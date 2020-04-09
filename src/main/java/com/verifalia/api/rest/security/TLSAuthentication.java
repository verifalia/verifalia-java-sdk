package com.verifalia.api.rest.security;

import static java.util.Objects.nonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;

import com.verifalia.api.common.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TLSAuthentication {

	/**
	 * Certificate alias
	 */
	private String certAlias;

	/**
	 * Certificate password
	 */
	private String certPassword;

	/**
	 * Identity store JKS file
	 */
	private File identityStoreJksFile;

	/**
	 * Trust key store JKS file
	 */
	private File trustKeyStoreJksFile;

	/**
	 * SSL Connection socket factory
	 */
	private SSLConnectionSocketFactory sslConnectionSocketFactory;

	/**
	 * Constructs an object for TLS authentication to authenticate API client
	 * @param certAlias Certificate alias
	 * @param certPassword Certificate password
	 * @param identityStoreJksFile Identity key-store JKS file
	 * @param trustKeyStoreJksFile Trust key-store JKS file
	 * @throws Exception
	 */
	public TLSAuthentication(String certAlias, String certPassword, File identityStoreJksFile, File trustKeyStoreJksFile){
		this.certAlias = certAlias;
		this.certPassword = certPassword;
		this.identityStoreJksFile = identityStoreJksFile;
		this.trustKeyStoreJksFile = trustKeyStoreJksFile;
	}

	/**
	 * Gets SSL connection socket factory object for TLS Authentication
	 * @return SSLConnectionSocketFactory SSL connection socket factory
	 * @throws Exception
	 */
	public SSLConnectionSocketFactory getSSlConnectionSocketFactory() throws IOException {
		if(!nonNull(this.sslConnectionSocketFactory)){
			try {
				this.sslConnectionSocketFactory = getSSlConnectionSocketFactory(this.certAlias, this.certPassword,
					this.identityStoreJksFile, this.trustKeyStoreJksFile);
			} catch(Exception e){
				throw new IOException(e);
			}
		}
		return this.sslConnectionSocketFactory;
	}

	private SSLConnectionSocketFactory getSSlConnectionSocketFactory(final String certAlias, final String certPassword,
			File identityStoreJksFile, File trustKeyStoreJksFile) throws Exception {
		// Load identity key store
		KeyStore identityKeyStore = KeyStore.getInstance(Constants.TLS_AUTHENTICATION_JKS);
		FileInputStream identityKeyStoreFile = new FileInputStream(identityStoreJksFile);
		identityKeyStore.load(identityKeyStoreFile, certPassword.toCharArray());

		// Load trust key store
		KeyStore trustKeyStore = KeyStore.getInstance(Constants.TLS_AUTHENTICATION_JKS);
		FileInputStream trustKeyStoreFile = new FileInputStream(trustKeyStoreJksFile);
		trustKeyStore.load(trustKeyStoreFile, certPassword.toCharArray());

		// Load SSL context
	    SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(identityKeyStore, certPassword.toCharArray(), new PrivateKeyStrategy() {
	    	public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
	    		return certAlias;
	    	}
	    }).loadTrustMaterial(trustKeyStore, null).build();

	    // Initialize socket factory
	    return new SSLConnectionSocketFactory(sslContext,
	    	new String[]{Constants.TLS_AUTHENTICATION_VERSION_1_2, Constants.TLS_AUTHENTICATION_VERSION_1_1},
	    	null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
	}
}
