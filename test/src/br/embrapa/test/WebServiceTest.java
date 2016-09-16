/**
 * Copyright © 2014 Embrapa. All Rights Reserved.
 *
 * Developed by Laboratory for Precision Livestock, Environment and Software Engineering (PLEASE Lab)
 * of Embrapa Beef Cattle at Campo Grande - MS - Brazil.
 * 
 * @see http://please.cnpgc.embrapa.br
 * 
 * @author Jairo Ricardes Rodrigues Filho <jairocgr@gmail.com>
 * @author Camilo Carromeu <camilo.carromeu@embrapa.br>
 * 
 * @version 14.05-1-alpha
 */

package br.embrapa.test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.filter.log.ErrorLoggingFilter;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;

public class WebServiceTest
{
	private static String API_URI = "http://localhost:8090/api";

	private static String APPLICATION_ID = "mobile";
	private static String APPLICATION_TOKEN = "mnOTyIPfrpkMh8w1qDFOl9VvHii4XoZedFUgTxrQqmB7zuPS6CXH1hKsz0ilGgdf";

	private static String CLIENT_ID = "1";
	private static String CLIENT_PRIVATE_KEY = "2GD61X0PGHJP3GNQ";
	
	private static String USER_ID = "camilo";
	private static String USER_PASSWORD = "123";

	private Integer timestamp;

	private HttpEmbrapaAuthCredential userCredential;
	private HttpEmbrapaAuthCredential clientCredential;
	private HttpEmbrapaAuthCredential applicationCredential;

	@Before
	public void setUp ()
	{
		timestamp = (int) (new Date ().getTime () / 1000) - 60;

		userCredential = new HttpEmbrapaAuthCredential (timestamp, USER_ID, sha1 (USER_PASSWORD));
		clientCredential = new HttpEmbrapaAuthCredential (timestamp, CLIENT_ID, CLIENT_PRIVATE_KEY);
		applicationCredential = new HttpEmbrapaAuthCredential (timestamp, APPLICATION_ID, APPLICATION_TOKEN);
	}

	@Test
	public void tAuthCredentials () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/auth");
	}

	@Test
	public void tDisambiguation () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("14")).when ().get (API_URI + "/disambiguation");
	}

	@Test
	public void tGetListOfAlerts () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/alerts");
	}

	@Test
	public void tDeleteAlert () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().delete (API_URI + "/alert/10");
	}

	@Test
	public void tMarkReadAlert () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().put (API_URI + "/alert/1");
	}

	@Test
	public void tRegisterUserByGoogleAccount () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.param ("device", "Samsung Galaxy Note 3")

		.param ("email", "camilo@carromeu.com")

		.param ("token", applicationCredential.encrypt ("1/nTEtiiRmyVz1rD_wICnNGdhu7ds70vROGt27Nzo3Srw"))

		.expect ().statusCode (200).content (equalTo ("")).when ().post (API_URI + "/register");
	}

	@Test
	public void tRegisterDeviceToGoogleCloudMessage ()
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())
		
		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.param ("gcm", "abcdeFGHIJ123456-987aq")

		.expect ().statusCode (200)

		.when ().post (API_URI + "/gcm");
	}

	@Test
	public void tGetItem () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/crud/1");
	}

	@Test
	public void tGetListOfItems () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/crud/list/0");
	}

	@Test
	public void tGetActiveItems ()
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().get (API_URI + "/crud/active");
	}

	@Test
	public void tPostItem () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.param ("code", "654.28")

		.param ("c_string", "Teste: Envio de um POST genérico - CRIAÇÃO.")

		.param ("c_boolean", "1")

		.param ("c_date", "1400445481")

		.param ("c_text", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis, ligula non fringilla egestas, enim risus lacinia dolor, nec rhoncus dolor sapien eleifend risus. In interdum tristique nisi, pharetra tristique felis fermentum congue. Morbi mollis tincidunt lorem. Aliquam erat volutpat. Nulla tempor lorem at nisi vehicula pellentesque. Sed ultrices semper egestas. Donec mi lectus, ultricies ut ultricies non, aliquet sit amet neque. Mauris porttitor scelerisque felis, at tincidunt sem blandit in. Nunc sit amet nunc fringilla, lacinia justo nec, lobortis ligula. Donec aliquam venenatis mattis.")

		.param ("c_color", "990000")

		.param ("c_cpf", "40986255718")

		.param ("c_cnpj", "008203362000191")

		.param ("c_enum", "_D_")

		.param ("c_fck", "Sed imperdiet enim non sodales pretium. Nullam vestibulum, urna id venenatis fermentum, risus enim lobortis odio, quis interdum risus nisi in ante. Nulla facilisi. In orci lectus, adipiscing a risus ac, sollicitudin pharetra turpis. Nam vel ultrices sapien, in sagittis sem. Quisque sagittis luctus tincidunt. Morbi mauris risus, adipiscing sit amet urna id, pharetra rutrum enim. Proin aliquet non libero nec cursus. Aliquam erat volutpat.")

		.param ("c_cep", "79888333")

		.param ("c_city", "7385")

		.param ("c_state", "MS")

		.param ("c_select", "CAT05")

		.param ("c_float", "123456.789")

		.param ("c_integer", "987654321")

		.param ("last_change", "1400445490")

		.multiPart ("c_file", new File (getClass ().getResource ("/Koala.jpg").getPath ()))

		.expect ().statusCode (200).content (equalTo ("")).when ().post (API_URI + "/crud");
	}
	
	@Test
	public void tPutItem () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())
		
		.formParam ("c_string", "Teste: Envio de PUT genérico - CRIAÇÃO e EDIÇÃO.")

		.formParam ("c_boolean", "0")

		.formParam ("c_date", "1400445481")

		.formParam ("c_text", "Lórem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis, ligula non fringilla egestas, enim risus lacinia dolor, nec rhoncus dolor sapien eleifend risus. In interdum tristique nisi, pharetra tristique felis fermentum congue. Morbi mollis tincidunt lorem. Aliquam erat volutpat. Nulla tempor lorem at nisi vehicula pellentesque. Sed ultrices semper egestas. Donec mi lectus, ultricies ut ultricies non, aliquet sit amet neque. Mauris porttitor scelerisque felis, at tincidunt sem blandit in. Nunc sit amet nunc fringilla, lacinia justo nec, lobortis ligula. Donec aliquam venenatis mattis.")

		.formParam ("c_color", "990000")

		.formParam ("c_cpf", "40986255718")

		.formParam ("c_cnpj", "008203362000191")

		.formParam ("c_enum", "_D_")

		.formParam ("c_fck", "Sed imperdiet enim non sodales pretium. Nullam vestibulum, urna id venenatis fermentum, risus enim lobortis odio, quis interdum risus nisi in ante. Nulla facilisi. In orci lectus, adipiscing a risus ac, sollicitudin pharetra turpis. Nam vel ultrices sapien, in sagittis sem. Quisque sagittis luctus tincidunt. Morbi mauris risus, adipiscing sit amet urna id, pharetra rutrum enim. Proin aliquet non libero nec cursus. Aliquam erat volutpat.")

		.formParam ("c_cep", "79888333")

		.formParam ("c_city", "7385")

		.formParam ("c_state", "MS")

		.formParam ("c_select", "CAT05")

		.formParam ("c_float", "123456.789")

		.formParam ("c_integer", "987654321")

		.formParam ("last_change", "1406414325")

		.multiPart ("c_file", new File (getClass ().getResource ("/Koala.jpg").getPath ()))

		.expect ().statusCode (200).content (equalTo ("")).when ().put (API_URI + "/crud/500.7");
	}

	@Test
	public void tDeleteItem () throws UnsupportedEncodingException
	{
		given ().filter (new RequestLoggingFilter ()).filter (new ResponseLoggingFilter ()).filter (new ErrorLoggingFilter ())

		.header ("x-embrapa-auth-timestamp", String.valueOf (timestamp)).and ()

		.header ("x-embrapa-auth-application-id", APPLICATION_ID).and ().header ("x-embrapa-auth-application-signature", applicationCredential.signature ())

		.header ("x-embrapa-auth-client-id", CLIENT_ID).and ().header ("x-embrapa-auth-client-signature", clientCredential.signature ())

		.header ("x-embrapa-auth-user-id", USER_ID).and ().header ("x-embrapa-auth-user-signature", userCredential.signature ())

		.expect ().statusCode (200).content (equalTo ("")).when ().delete (API_URI + "/crud/432.19");
	}

	public String sha1 (String input)
	{
		try
		{
			return DigestUtils.sha1Hex (input.getBytes ("UTF-8"));

		}
		catch (Exception e)
		{
			throw new RuntimeException (e);
		}
	}

	@After
	public void tearDown ()
	{}
}