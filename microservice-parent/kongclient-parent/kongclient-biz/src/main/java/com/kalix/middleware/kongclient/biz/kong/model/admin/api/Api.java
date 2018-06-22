package com.kalix.middleware.kongclient.biz.kong.model.admin.api;

import com.google.gson.annotations.SerializedName;
//import lombok.Data;

import java.util.List;

/**
 * Created by vaibhav on 13/06/17.
 */
//@Data
public class Api {

	@SerializedName("created_at")
	private Long createdAt;

	@SerializedName("hosts")
	private Object hosts; //sometimes array, sometimes map
//	private List<String> hosts = null;

	@SerializedName("uris")
	private List<String> uris;

	@SerializedName("methods")
	private List<String> methods;

	@SerializedName("http_if_terminated")
	private Boolean httpIfTerminated;

	@SerializedName("https_only")
	private Boolean httpsOnly;

	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("preserve_host")
	private Boolean preserveHost;

	@SerializedName("retries")
	private Integer retries;

	@SerializedName("strip_uri")
	private Boolean stripUri;

	@SerializedName("upstream_connect_timeout")
	private Integer upstreamConnectTimeout;

	@SerializedName("upstream_read_timeout")
	private Integer upstreamReadTimeout;

	@SerializedName("upstream_send_timeout")
	private Integer upstreamSendTimeout;

	@SerializedName("upstream_url")
	private String upstreamUrl;

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Object getHosts() {
		return hosts;
	}

	public void setHosts(Object hosts) {
		this.hosts = hosts;
	}

	public List<String> getUris() {
		return uris;
	}

	public void setUris(List<String> uris) {
		this.uris = uris;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public Boolean getHttpIfTerminated() {
		return httpIfTerminated;
	}

	public void setHttpIfTerminated(Boolean httpIfTerminated) {
		this.httpIfTerminated = httpIfTerminated;
	}

	public Boolean getHttpsOnly() {
		return httpsOnly;
	}

	public void setHttpsOnly(Boolean httpsOnly) {
		this.httpsOnly = httpsOnly;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getPreserveHost() {
		return preserveHost;
	}

	public void setPreserveHost(Boolean preserveHost) {
		this.preserveHost = preserveHost;
	}

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public Boolean getStripUri() {
		return stripUri;
	}

	public void setStripUri(Boolean stripUri) {
		this.stripUri = stripUri;
	}

	public Integer getUpstreamConnectTimeout() {
		return upstreamConnectTimeout;
	}

	public void setUpstreamConnectTimeout(Integer upstreamConnectTimeout) {
		this.upstreamConnectTimeout = upstreamConnectTimeout;
	}

	public Integer getUpstreamReadTimeout() {
		return upstreamReadTimeout;
	}

	public void setUpstreamReadTimeout(Integer upstreamReadTimeout) {
		this.upstreamReadTimeout = upstreamReadTimeout;
	}

	public Integer getUpstreamSendTimeout() {
		return upstreamSendTimeout;
	}

	public void setUpstreamSendTimeout(Integer upstreamSendTimeout) {
		this.upstreamSendTimeout = upstreamSendTimeout;
	}

	public String getUpstreamUrl() {
		return upstreamUrl;
	}

	public void setUpstreamUrl(String upstreamUrl) {
		this.upstreamUrl = upstreamUrl;
	}
}
