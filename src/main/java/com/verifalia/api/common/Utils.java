package com.verifalia.api.common;

import static java.util.Objects.nonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

public class Utils {
    /**
     * <p>Removes one newline from end of a String if it's there,
     * otherwise leave it alone.  A newline is &quot;{@code \n}&quot;,
     * &quot;{@code \r}&quot;, or &quot;{@code \r\n}&quot;.</p>
     * @param str  the String to chomp a newline from, may be null
     * @return String without newline, {@code null} if null String input
     */
    public static String chomp(final String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        if (str.length() == 1) {
            final char ch = str.charAt(0);
            if (ch == Constants.CR || ch == Constants.LF) {
                return "";
            }
            return str;
        }

        int lastIdx = str.length() - 1;
        final char last = str.charAt(lastIdx);

        if (last == Constants.LF) {
            if (str.charAt(lastIdx - 1) == Constants.CR) {
                lastIdx--;
            }
        } else if (last != Constants.CR) {
            lastIdx++;
        }
        return str.substring(0, lastIdx);
    }

    public static URI getHttpUri(String scheme, String host, String path, Map<String, String> paramMap){
    	URIBuilder builder = new URIBuilder();
    	try {
    		if(!StringUtils.isBlank(scheme)){
    			builder.setScheme(scheme);
    		}
    		if(!StringUtils.isBlank(host)){
    			builder.setHost(host);
    		}
    		if(!StringUtils.isBlank(path)){
    			builder.setPath(path);
    		}
			if(nonNull(paramMap) && paramMap.size() > 0){
				Iterator<String> paramMapIter = paramMap.keySet().iterator();
				while(paramMapIter.hasNext()){
					String paramKey = paramMapIter.next();
					String paramValue = paramMap.get(paramKey);
					if(!StringUtils.isEmpty(paramValue)){
						builder.setParameter(paramKey, paramValue);
					}
				}
			}
			return builder.build();
    	} catch(URISyntaxException e){
    		return null;
    	}
    }

    public static String convertStringIteratorToString(Iterable<String> iterable, String separator){
    	if(nonNull(iterable)){
    		return StringUtils.join(iterable, separator);
    	}
    	return StringUtils.EMPTY;
    }
}
