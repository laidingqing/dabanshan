organization in ThisBuild := "com.dabanshan"
version in ThisBuild := "1.0"

scalaVersion in ThisBuild := "2.11.8"

val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % "3.3"
val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val accord = "com.wix" %% "accord-core" % "0.6.1"
val base64 = "me.lessis" %% "base64" % "0.2.0"
val jwt = "com.pauldijou" %% "jwt-play-json" % "0.12.1"

lazy val root = (project in file("."))
  .aggregate(webGateway, commons, userApi, userImpl, productApi, productImpl, cookbookApi)

lazy val commons = (project in file("commons"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.github.rholder.fauxflake" % "fauxflake-core" % "1.1.0",
      lagomScaladslApi,
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
      scalaTest
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
      scalaTest

    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(productApi, commons)

lazy val cookbookApi = (project in file("cookbook-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val webGateway = (project in file("web-gateway"))
  .enablePlugins(PlayScala, LagomPlay)
  .dependsOn(userApi)
  .settings(
    routesGenerator := InjectedRoutesGenerator,
    libraryDependencies ++= Seq(
      "org.webjars" % "react" % "0.14.3",
      "org.webjars" % "react-router" % "1.0.3",
      "org.webjars" % "foundation" % "5.3.0"
    )
  )

lagomCassandraEnabled in ThisBuild := false
lagomUnmanagedServices in ThisBuild := Map("cas_native" -> "http://localhost:9042")


def commonSettings: Seq[Setting[_]] = Seq(
)