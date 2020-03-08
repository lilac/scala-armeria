resolvers += "aliyun public" at "https://maven.aliyun.com/repository/public"
resolvers += "google" at "https://maven-central.storage.googleapis.com/maven2"
externalResolvers := Resolver.combineDefaultResolvers(resolvers.value.toVector, false)


addSbtPlugin("com.github.gseitz" % "sbt-protobuf" % "0.6.5")
