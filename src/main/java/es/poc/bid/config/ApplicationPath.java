package es.poc.bid.config;

import es.poc.bid.utils.HttpMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.poc.bid.utils.HttpMethod.*;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public enum ApplicationPath {
    TOP_BIDS(GET, Pattern.compile("/(\\d+)/topBidList")),
    BID_ITEM(POST, Pattern.compile("/(\\d+)/bid\\?sessionKey=(\\w+)")),
    LOGIN_PATH(GET, Pattern.compile("/(\\w+)/login"));

    private HttpMethod httpMethod;

    private Pattern pattern;

    public Matcher getMatcher(String string){
        return pattern.matcher(string);
    }

    public HttpMethod getHttpMethod(){
        return httpMethod;
    }
    ApplicationPath(HttpMethod httpMethod, Pattern pattern){
        this.httpMethod = httpMethod;
        this.pattern =pattern;
    }
}

