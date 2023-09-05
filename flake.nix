{
  description = "LibreCybernetics Monorepo Flake";

  inputs.devshell.url    = "github:numtide/devshell";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, devshell, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;

          overlays = [ devshell.overlays.default ];
        };
        jdk = pkgs.jdk11;
      in rec {
        config = rec {
          packages = with pkgs; [
            async-profiler
            jdk
            llvmPackages_15.clang
            nodejs
            (sbt.override {
              jre = jdk;
            })
          ];

          env = [{
            name = "JAVA_HOME";
            value = "${jdk.home}";
          }
          {
            name = "LD_LIBRARY_PATH";
            value = nixpkgs.lib.makeLibraryPath config.packages;
          }];
        };

        devShell = pkgs.devshell.mkShell {
          env = config.env;
          packages = config.packages;
        };
      }
    );
}
