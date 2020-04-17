package com.verifalia.api.baseURIProviders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ClientCertificateBaseURIProvider extends BaseURIProvider {
    private ArrayList<URI> baseURIs;

    public ClientCertificateBaseURIProvider() {
        try {
            baseURIs = new ArrayList<URI>() {{
                add(new URI("https://api-cca-1.verifalia.com"));
                add(new URI("https://api-cca-2.verifalia.com"));
                add(new URI("https://api-cca-3.verifalia.com"));
            }};
        } catch (URISyntaxException e) {
        }
    }

    public List<URI> provideBaseURIs() {
        return baseURIs;
    }
}
