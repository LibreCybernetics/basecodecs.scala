package dev.librecybernetics.data

opaque type Base64URLSafe = String

private val proofSubtype = summon[Base64URLSafe <:< String]

object Base64URLSafe extends BaseConversions[Base64URLSafe](codec.Base64URLSafe):
  given <:<[Base64URLSafe, String] = proofSubtype
end Base64URLSafe
