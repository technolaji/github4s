package com.fortysevendeg.github4s.free.domain

case class Gist (
    id: String,
    description: String,
    owner: Collaborator,
    public: Boolean)
