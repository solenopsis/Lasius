# Lasius

Welcome to Lasius - a Java utility framework for SFDC.  This project contains many useful features, but chief among them is automatic session management to your SFDC web services (custom, enterprise, partner, metadata and tooling).  By this we mean using credentials (user name, password, security token SFDC API version, and URL), we can provide:
* Automatic login.
* Automatic relogin should a session id become invalid.
* Concurrent threaded access to SFDC per session id.
* Multiplexed session ids for scaling up simultaneous concurrent calls to SFDC.

The most interesting thing to consider in the aforementioned statements is there is nothing special you must do other than have your SFDC WSDL and use wsimport to generate your client Java code.  Once you've done this, in a matter of a few lines of code, you can leverage the above bullet points.  The following sections will show you all you that's involved.

## Credentials

[Credentials] (https://github.com/solenopsis/Lasius/blob/master/common/src/main/java/org/solenopsis/lasius/credentials/Credentials.java) are nothing more than a user name, password, security token, API version and URL (the  SFDC URL to use when we login - ie https://test.salesforce.com or https://login.salesforce.com).  There exists an interface aptly entitled [Credentials] (https://github.com/solenopsis/Lasius/blob/master/common/src/main/java/org/solenopsis/lasius/credentials/Credentials.java) and a few implementations:
* [Default Credentials] (https://github.com/solenopsis/Lasius/blob/master/common/src/main/java/org/solenopsis/lasius/credentials/DefaultCredentials.java):  simple holder of the aforementioned items such as user name, password, etc.
* [Properties Credentials] (https://github.com/solenopsis/Lasius/blob/master/common/src/main/java/org/solenopsis/lasius/credentials/PropertiesCredentials.java):  uses a [property manager] (https://github.com/FlossWare/java/tree/master/utils/src/main/java/org/flossware/util/properties), from the [FlossWare java library] (https://github.com/FlossWare/java), to load properties containing the aforementioned items such as user name, password, etc.

### Example

#### /tmp/myuser/creds/single/lone-user.properties

```properties
username = loneuser@some.company.com
password = somepassword
token = abcdefghijklmnopqrstuvwx
url = https://test.salesforce.com
apiVersion = 30.0
```

#### Java

This snippet will load the aforementioned lone-user.properties file into a [Credentials] (https://github.com/solenopsis/Lasius/blob/master/common/src/main/java/org/solenopsis/lasius/credentials/Credentials.java) instance.

```java
final Credentials credentials = new PropertiesCredentials(new FilePropertiesMgr("/tmp/myuser/creds/single/lone-user.properties"));
```

## Security Managers

[Security managers] (https://github.com/solenopsis/Lasius/tree/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/security) are used to login and logout of SFDC.  All security managers provide an implementation of [LoginResult] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/security/LoginResult.java) which represents the login result one gets when logging into SFDC using either the enterprise, partner or tooling web services.  Coincidentally, there are three security manager implementations in Lasius corresponding to them:
* [Enterprise Security Manager] (https://github.com/solenopsis/Lasius/tree/master/wsutils/enterprise/src/main/java/org/solenopsis/lasius/wsimport/enterprise/security/EnterpriseSecurityMgr.java)
* [Partner Security Manager] (https://github.com/solenopsis/Lasius/tree/master/wsutils/partner/src/main/java/org/solenopsis/lasius/wsimport/partner/security/PartnerSecurityMgr.java)
* [Tooling Security Manager] (https://github.com/solenopsis/Lasius/tree/master/wsutils/tooling/src/main/java/org/solenopsis/lasius/wsimport/tooling/security/ToolingSecurityMgr.java)

A [security manager] (https://github.com/solenopsis/Lasius/tree/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/security) is used by [session managers] (https://github.com/solenopsis/Lasius/tree/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/mgr) to login (if not already logged in) or log out (using the resetSession() method).  The result of a login is a [Session] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java).  Please see the next section for more information. 
## Sessions

Once one logs into into SFDC (via a [Security Manager] (https://github.com/solenopsis/Lasius/tree/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/security)), a [Session] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java) is created that holds the [login result] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/security/LoginResult.java), and SFDC session id.  Additionally, a [session] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java) can provide the mechanism for locking and unlocking to help control simultaneous calls to SFDC web services (think of a semaphore).  The call to [lock()] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java#L22) will actually block should their be 0 [remaing locks available] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java#L34).  Calling [unlock()] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java#L27) will allow any threads being block when attempting to [lock()] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java#L22) proceed.

It is highly unlikely you will ever use this class directly.  It is actually used by proxy ports as described below.

## Session Managers

[Session managers] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/mgr/SessionMgr.java) utilize [credentials] (https://github.com/solenopsis/Lasius/blob/master/common/src/main/java/org/solenopsis/lasius/credentials/Credentials.java) and [security managers] (https://github.com/solenopsis/Lasius/tree/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/security) to:
* Manage [sessions] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/Session.java).
* Perform automatic logins when returning a session.  If no session exists, the login will happen at that time.
* Reset sessions: disregards the current session and will perform a login.  Please note this is only applicable if you attempt to reset a good "current" session.

There are currently two implementations of session managers:
* [Single session manager] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/mgr/SingleSessionMgr.java):  Deals with a single credential and a lone session.
* [Multi session manager] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/mgr/MultiSessionMgr.java):  Allows you to maintain N session managers using N credentials.

## Web Services

When using wsimport against a WSDL, client Java code is generated containing a subclass of [Service] (http://docs.oracle.com/javase/7/docs/api/javax/xml/ws/Service.html).  This subclass will contain a "port" method that one uses to perform actual web service calls.  For example, the following snippet represents the [service] (http://docs.oracle.com/javase/7/docs/api/javax/xml/ws/Service.html) generated code for an enterprise WSDL:

```java
@WebServiceClient(name = "SforceService", targetNamespace = "urn:enterprise.soap.sforce.com", wsdlLocation = "file:/home/sfloess/Development/personal/solenopsis/Lasius/wsutils/wsdls/src/main/resources/wsdl/Lasius-enterprise.wsdl")
public class SforceService
    extends Service
{
	@WebEndpoint(name = "Soap")
	public Soap getSoap() {
		return super.getPort(new QName("urn:enterprise.soap.sforce.com", "Soap"), Soap.class);
	}
}
```

Here is a snippet of wsimport generated java code for the tooling WSDL:

```java

@WebServiceClient(name = "SforceServiceService", targetNamespace = "urn:tooling.soap.sforce.com", wsdlLocation = "file:/home/sfloess/Development/personal/solenopsis/Lasius/wsutils/wsdls/src/main/resources/wsdl/Lasius-tooling.wsdl")
public class SforceServiceService
    extends Service
{
    @WebEndpoint(name = "SforceService")
    public SforceServicePortType getSforceService() {
        return super.getPort(new QName("urn:tooling.soap.sforce.com", "SforceService"), SforceServicePortType.class);
    }
}
```

The port is denoted via the annotated method for @WebEndpoint and above is:
* the method getSoap() for the enterprise WSDL.
* the method getSforceService() for the tooling WSDL.

When making SFDC web service calls, one musts:
* set the session id (generated from login) into the SOAP header of the port.
* set the server URL from login as the end point.  The URL denoted above for credentials is the login URL.  Once you actually login, you are presented with a server URL in your Login Result - this is the one you want to use.

Lasius provides a number of convenient ways to deal with ports as described below.

### Abstraction

Lasius make use of the [FlossWare Web Service package] (https://github.com/FlossWare/java/tree/master/wsutils/service/src/main/java/org/flossware/wsimport/service) to simplify creating ports.  Above we illustrated two different methods for returning the ports on the enterprise and tooling WSDLs.  The [FlossWare Service package] (https://github.com/FlossWare/java/tree/master/wsutils/service/src/main/java/org/flossware/wsimport/service) will examine a [Service] (http://docs.oracle.com/javase/7/docs/api/javax/xml/ws/Service.html)  subclass to find the ports for a [Service] (http://docs.oracle.com/javase/7/docs/api/javax/xml/ws/Service.html) based upon the @WebEndpoint annotation and use that to create ports uniformly and easily, encapsulating this to one method [getPort()] (https://github.com/FlossWare/java/blob/master/wsutils/service/src/main/java/org/flossware/wsimport/service/WebService.java#L50).  This simplification makes port management for any SFDC custom web service trivial.

#### Example

To illustrate, we'll show how to deal with both the enterprise and tooling WSDLs above.  Please note we are denoting that the WSDLs are located in the classpath within a directory entitled "wsdl" as seen below:

```java
final WebService<Soap> enterpriseWebService = new GenericWebService("wsdl/enterprise.wsdl", SforceService.class);
final WebService<SforceServicePortType> toolingWebService = new GenericWebService("wsdl/tooling.wsdl", SforceServiceService.class);

final Soap enterprisePort = enterpriseWebService.getPort();
final SforceServicePortType toolingPort = toolingWebService.getPort();
```

Even better:

```java
final Soap enterprisePort = new GenericWebService<Soap>("wsdl/enterprise.wsdl", SforceService.class).getPort();
final SforceServicePortType toolingPort = new GenericWebService<SforceServicePortType>("wsdl/tooling.wsdl", SforceServiceService.class);
```

### Port Management

Lasius provides a number of utility classes for utilizing SFDC web service ports (please see above for the definition of "port" here as it is not related to Sockets):

* [Salesforce Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/util/SalesforceWebServiceUtil.java)
* [Custom Web Service (Developer Designed) Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/CustomWebServiceUtil.java)
* [Enterprise Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/EnterpriseWebServiceUtil.java)
* [Metadata Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/MetadataWebServiceUtil.java)
* [Partner Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/PartnerWebServiceUtil.java)
* [Tooling Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/ToolingWebServiceUtil.java)

The [Salesforce Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/util/SalesforceWebServiceUtil.java) is a very generic utility class.  It's useful but is mostly leveraged by the other utility classes listed in the bulleted section.  While you can use this, it's unlikely you ever will.

#### Unproxied Ports

Unproxied ports are those ports that are used without concern for:
* Sharing session ids across threads.
* Relogins when session ids become invalid.

They are great when you need to communicate with SFDC simply and easily.  Be aware when using any of the following web service utilities in an unproxied capacity, you will be automatically logged in so your web service calls can proceed for you:

* [Custom Web Service (Developer Designed) Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/CustomWebServiceUtil.java)
* [Enterprise Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/EnterpriseWebServiceUtil.java)
* [Metadata Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/MetadataWebServiceUtil.java)
* [Partner Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/PartnerWebServiceUtil.java)
* [Tooling Web Service Util] (https://github.com/solenopsis/Lasius/blob/sfloess_issue-33/wsutils/utils/src/main/java/org/solenopsis/lasius/wsimport/util/ToolingWebServiceUtil.java)

##### Example

Assume you want to utilize your company's enterprise.wsdl and you've stored it in your classpath within a "wsdl" directory:

```java
final Credentials credentials = new DefaultCredentials("https://test.salesforce.com", "user1", "password", "abcdefghijklmnopqrstuvwx", "30.0");
final Soap enterprisePort = EnterpriseWebServiceUtil.createPort(credentials, "wsdl/enterprise.wsdl", SforceService.class);
```

Now when you use enterprisePort:
* You will automtically be logged in.
* The session id will be set on the port.
* The server URL from login will be set and your calls will hit the correct SFDC host.

#### Proxied Ports

Proxied ports are useful when you:
* Allow for concurrent threaded access to SFDC web services for a session id.
* Multiplexing many SFDC users to scale up simultaneous concurrent calls.
* Long running applications where session ids may become stale (dreaded invalid session id SOAP Fault).
* Using more than one web service across session id.

Simultaneous concurrent calls per session Id is based upon SFDC API releases.  Presently Lasius supports up to 10 concurrent calls per session id.  Please note where appropriate should the number of calls for a session Id be greater than 10, we will either block (in the case of a [SingleSessionMgr] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/mgr/SingleSessionMgr.java)) or attempt to use another SessionMgr (when using a [MultiSessionMgr] (https://github.com/solenopsis/Lasius/blob/master/wsutils/common/src/main/java/org/solenopsis/lasius/wsimport/common/session/mgr/MultiSessionMgr.java)).

##### Example

Assume you want to utilize your company's enterprise.wsdl, tooling.wsdl and metdata.wsdl.  Also assume you've stored those WSDL's within a "wsdl" directory.

###### Single SFDC User

```java
final Credentials credentials = new DefaultCredentials("https://test.salesforce.com", "user1", "password", "abcdefghijklmnopqrstuvwx", "30.0");
final SingleSessionMgr sessionMgr = new SingleSessionMgr(credenials, new EnterpriseSecurityMgr());

final Soap enterprisePort = EnterpriseWebServiceUtil.createEnterpriseProxyPort(sessionMgr, "wsdl/enterprise.wsdl", SforceService.class);
final SforceServicePortType.class toolingPort = ToolingWebServiceUtil.createToolingProxyPort(sessionMgr, "wsdl/tooling.wsdl", SforceServicePortType.class);
final MetadataPortType metadataPort = MetadataWebServiceUtil.createMetadataProxyPort(sessionMGr, "wsdl/metadata.wsdl", MetadataService.class);
```

Now you can:
* use all the ports in a threaded capacity - but for one SFDC user.  
* never exceed 10 simultaneous calls as the session manager will block any call until any additional calls complete.
* automatically logged in.
* automatically relogin over time when your session id becomes stale (invalid session id)

###### Multiple SFDC Users

```java
final Credentials credentials1 = new DefaultCredentials("https://test.salesforce.com", "user1", "password", "abcdefghijklmnopqrstuvwx", "30.0");
final Credentials credentials2 = new DefaultCredentials("https://test.salesforce.com", "user2", "password", "abcdefghijklmnopqrstuvwx", "30.0");
final Credentials credentials3 = new DefaultCredentials("https://test.salesforce.com", "user3", "password", "abcdefghijklmnopqrstuvwx", "30.0");
final Credentials credentials4 = new DefaultCredentials("https://test.salesforce.com", "user4", "password", "abcdefghijklmnopqrstuvwx", "30.0");

final MultiSessionMgr sessionMgr = new MultiSessionMgr(new Credentials[] {credentials1, credentials2, credentials3, credentials4}, new EnterpriseSecurityMgr());

final Soap enterprisePort = EnterpriseWebServiceUtil.createEnterpriseProxyPort(sessionMgr, "wsdl/enterprise.wsdl", SforceService.class);
final SforceServicePortType.class toolingPort = ToolingWebServiceUtil.createToolingProxyPort(sessionMgr, "wsdl/tooling.wsdl", SforceServicePortType.class);
final MetadataPortType metadataPort = MetadataWebServiceUtil.createMetadataProxyPort(sessionMGr, "wsdl/metadata.wsdl", MetadataService.class);
```

Now you can:
* use all the ports in a threaded capacity - across four SFDC users.
* never exceed 40 simultaneous calls as the session manager will multiplex calls across all four SFDC users!  When you exceed 40, the calls will be blocked until one session manager becomes free.  Please note your upper bounds on number of concurrent calls is:  10 x the-number-of-credentials.  Above we used four credentials therefore our total concurrent calls is:  10 x 4 = 40!
* automatically logged in.
* automatically relogin over time when a session id from one of the sessions becomes stale (invalid session id).

It's important to note this works equally well with your own custom SFDC web services - the level of effort is exactly the same!