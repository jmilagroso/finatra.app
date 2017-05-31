package finatra.app.filters

import java.util.{Calendar, Date}

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.twitter.finagle.{Filter, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.{Logger, LoggerFactory}

/**
  * JWTFilter validates secured/non-secured endpoints for token.
  *
  * Created by jay <j.milagroso@gmail.com>
  */
class JWTFilter extends Filter[Request, Response, Request, Response] {

  // Type-safe config instance.
  def config: Config = ConfigFactory.load()

  // JWT Algorithm instance.
  def algorithm: Algorithm = Algorithm.HMAC256(config.getString("JWT.SECRET"))

  // Logger instance.
  def logger: Logger = LoggerFactory.getLogger(classOf[JWTFilter])

  /**
    * Issues new token.
    * @param id
    * @param claim
    * @param issuer
    * @param audience
    * @return
    */
  def issue(id: String, claim: String, issuer: String, audience: String): String = {
    val algorithm = Algorithm.HMAC256(config.getString("JWT.SECRET"));

    // Current Date + N Day(s)
    val dt = new Date();
    val c = Calendar.getInstance();
    c.setTime(dt);
    c.add(Calendar.DATE, config.getInt("JWT.EXPIRES.DAYS"));

    // Build token
    JWT.create
      .withJWTId(id)
      .withClaim("name", claim)
      .withIssuer(issuer)
      .withAudience(audience)
      .withExpiresAt(c.getTime)
      .sign(algorithm)
  }

  /**
    * Validates token.
    * @param token
    * @param id
    * @param claim
    * @param issuer
    * @param audience
    * @return
    */
  def valid(token: String, id: String, claim: String, issuer: String, audience: String): Boolean = {
    var valid = false

    try {
      val verifier = JWT.require(algorithm)
        .withJWTId(id)
        .withClaim("name", claim)
        .withIssuer(issuer)
        .withAudience(audience)
        .build //Reusable verifier instance
      verifier.verify(token)

      valid = true
    } catch {
      case e: Exception => {
        logger.info(e.printStackTrace().toString)
      }
    }

    valid
  }


  /**
    * Override apply method from Filter.
    * @param request
    * @param service
    * @return
    */
  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    service(request).map {
      response =>

        // Registers specified secured endpoints.
        // @TODO Check secured endpoints.
        val securedEndpoints = List(
          "/mustache",
          "/html",
          "/secured"

        )

        /*
        https://tools.ietf.org/html/rfc7519#section-4.1.7
        The "jti" (JWT ID) claim provides a unique identifier for the JWT.
        The identifier value MUST be assigned in a manner that ensures that
        there is a negligible probability that the same value will be
        accidentally assigned to a different data object; if the application
        uses multiple issuers, collisions MUST be prevented among values
        produced by different issuers as well.  The "jti" claim can be used
        to prevent the JWT from being replayed.  The "jti" value is a case-
        sensitive string.  Use of this claim is OPTIONAL.
        */
        val id = request.getParam("id")

        /*
        https://tools.ietf.org/html/rfc7519#section-4.2
        Claim Names can be defined at will by those using JWTs.  However, in
        order to prevent collisions, any new Claim Name should either be
        registered in the IANA "JSON Web Token Claims" registry established
        by Section 10.1 or be a Public Name: a value that contains a
        Collision-Resistant Name.  In each case, the definer of the name or
        value needs to take reasonable precautions to make sure they are in
        control of the part of the namespace they use to define the Claim
        Name.
        */
        val claim = request.getParam("claim")

        /*
        https://tools.ietf.org/html/rfc7519#section-4.1.1
        The "iss" (issuer) claim identifies the principal that issued the
        JWT.  The processing of this claim is generally application specific.
        The "iss" value is a case-sensitive string containing a StringOrURI
        value.  Use of this claim is OPTIONAL.
        */
        val issuer = request.getParam("issuer")

        /*
        https://tools.ietf.org/html/rfc7519#section-4.1.3
        The "aud" (audience) claim identifies the recipients that the JWT is
        intended for.  Each principal intended to process the JWT MUST
        identify itself with a value in the audience claim.  If the principal
        processing the claim does not identify itself with a value in the
        "aud" claim when this claim is present, then the JWT MUST be
        rejected.  In the general case, the "aud" value is an array of case-
        sensitive strings, each containing a StringOrURI value.  In the
        special case when the JWT has one audience, the "aud" value MAY be a
        single case-sensitive string containing a StringOrURI value.  The
        interpretation of audience values is generally application specific.
        Use of this claim is OPTIONAL.
         */
        val audience = request.getParam("audience")

        try {
          if(request.path == "/jwt/issue") {
            response.setContentTypeJson()
            response.setStatusCode(200)
            response.setContentString("{\"token\":\""+this.issue(id, claim, issuer, audience)+"\",\"token_type\":\"Bearer\"}")
          } else if(securedEndpoints.contains(request.path)) {

            val token = request.getParam("token")

            if(!valid(token, id, claim, issuer, audience)) {
              response.setContentTypeJson()
              response.setStatusCode(401)
              response.setContentString("{\"error\":\"Unauthorized request.\"}")
            }
          }
        } catch {
          case e: Exception => {
            response.setContentTypeJson()
            response.setStatusCode(500)
            response.setContentString(e.getLocalizedMessage)
          }
        }

        response
    }
  }

}
