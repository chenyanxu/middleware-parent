package com.kalix.middleware.kongclient.biz.kong.model.admin.plugin;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by fanhua on 2017-8-28
 */
//@Data
public class OAuth2Config {

	@SerializedName("provision_key")
	private String provisionKey;

	@SerializedName("scopes")
	private List<String> scopes;

	@SerializedName("mandatory_scope")
	private boolean mandatoryScope;

	@SerializedName("token_expiration")
	private int tokenExpiration;

	@SerializedName("anonymous")
	private String anonymous;

	@SerializedName("accept_http_if_already_terminated")
	private boolean acceptHttpIfAlreadyTerminated;

	@SerializedName("enable_authorization_code")
	private boolean enableAuthorizationCode;

	@SerializedName("enable_implicit_grant")
	private boolean enableImplicitGrant;

	@SerializedName("enable_password_grant")
	private boolean enablePasswordGrant;

	@SerializedName("enable_client_credentials")
	private boolean enableClientCredentials;

	@SerializedName("global_credentials")
	private boolean globalCredentials;

	@SerializedName("hide_credentials")
	private boolean hideCredentials;

	public String getProvisionKey() {
		return provisionKey;
	}

	public void setProvisionKey(String provisionKey) {
		this.provisionKey = provisionKey;
	}

	public List<String> getScopes() {
		return scopes;
	}

	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}

	public boolean isMandatoryScope() {
		return mandatoryScope;
	}

	public void setMandatoryScope(boolean mandatoryScope) {
		this.mandatoryScope = mandatoryScope;
	}

	public int getTokenExpiration() {
		return tokenExpiration;
	}

	public void setTokenExpiration(int tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}

	public String getAnonymous() {
		return anonymous;
	}

	public void setAnonymous(String anonymous) {
		this.anonymous = anonymous;
	}

	public boolean isAcceptHttpIfAlreadyTerminated() {
		return acceptHttpIfAlreadyTerminated;
	}

	public void setAcceptHttpIfAlreadyTerminated(boolean acceptHttpIfAlreadyTerminated) {
		this.acceptHttpIfAlreadyTerminated = acceptHttpIfAlreadyTerminated;
	}

	public boolean isEnableAuthorizationCode() {
		return enableAuthorizationCode;
	}

	public void setEnableAuthorizationCode(boolean enableAuthorizationCode) {
		this.enableAuthorizationCode = enableAuthorizationCode;
	}

	public boolean isEnableImplicitGrant() {
		return enableImplicitGrant;
	}

	public void setEnableImplicitGrant(boolean enableImplicitGrant) {
		this.enableImplicitGrant = enableImplicitGrant;
	}

	public boolean isEnablePasswordGrant() {
		return enablePasswordGrant;
	}

	public void setEnablePasswordGrant(boolean enablePasswordGrant) {
		this.enablePasswordGrant = enablePasswordGrant;
	}

	public boolean isEnableClientCredentials() {
		return enableClientCredentials;
	}

	public void setEnableClientCredentials(boolean enableClientCredentials) {
		this.enableClientCredentials = enableClientCredentials;
	}

	public boolean isGlobalCredentials() {
		return globalCredentials;
	}

	public void setGlobalCredentials(boolean globalCredentials) {
		this.globalCredentials = globalCredentials;
	}

	public boolean isHideCredentials() {
		return hideCredentials;
	}

	public void setHideCredentials(boolean hideCredentials) {
		this.hideCredentials = hideCredentials;
	}
}
