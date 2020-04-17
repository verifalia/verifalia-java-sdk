package com.verifalia.api.baseURIProviders;

import java.net.URI;
import java.util.List;

public abstract class BaseURIProvider {
    public abstract List<URI> provideBaseURIs();
}
