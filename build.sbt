organization in ThisBuild := "com.dabanshan"
version in ThisBuild := "1.0"

scalaVersion in ThisBuild := "2.11.8"

val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "3.3"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val accord = "com.wix" %% "accord-core" % "0.6.1"
val base64 = "me.lessis" %% "base64" % "0.2.0"
val jwt = "com.pauldijou" %% "jwt-play-json" % "0.12.1"
val cassandraDriverExtras = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.1.2"

lazy val root = (project in file("."))
  .aggregate(frontEnd, commons, userApi, userImpl, productApi, productImpl, balanceApi, cookbookApi)

lazy val commons = (project in file("commons"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.rholder.fauxflake" % "fauxflake-core" % "1.1.0",
      lagomScaladslApi,
      cassandraDriverExtras,
      lagomScaladslServer % Optional,
      playJsonDerivedCodecs,
      scalaTest,
      accord,
      base64,
      jwt
    )
  )

lazy val userApi = (project in file("user-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`commons`)

lazy val userImpl = (project in file("user-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      filters
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(userApi, commons)

lazy val productApi = (project in file("product-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  ).dependsOn(`commons`)

lazy val productImpl = (project in file("product-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      filters
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(productApi, commons)

lazy val balanceApi = (project in file("balance-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(`commons`)

lazy val cookbookApi = (project in file("cookbook-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val frontEnd = (project in file("front-end"))
  .enablePlugins(PlayJava, LagomPlay)
  .dependsOn(userApi)
  .settings(
    routesGenerator := InjectedRoutesGenerator,
    pipelineStages := Seq(rjs, digest, gzip),
    libraryDependencies ++= Seq(
      // WebJars
      "org.webjars" % "requirejs" % "2.3.2",
      "org.webjars" % "underscorejs" % "1.8.3",
      "org.webjars" % "jquery" % "1.12.4",
      "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
      "org.webjars" % "angular-ui-bootstrap" % "1.3.3",
      "org.webjars" % "angularjs" % "1.4.10" exclude("org.webjars", "jquery"),
      "org.webjars" % "angular-material" % "1.0.0-rc1"
    )
  )
lagomServiceGatewayPort in ThisBuild := 9010
lagomCassandraEnabled in ThisBuild := false
lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://localhost:9042")
lagomServiceLocatorEnabled in ThisBuild := true

def commonSettings: Seq[Setting[_]] = Seq(
)