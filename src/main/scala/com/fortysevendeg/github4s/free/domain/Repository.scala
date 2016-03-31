package com.fortysevendeg.github4s.free.domain

case class Repository(
    id: Int,
    owner: Collaborator,
    name: String
)
