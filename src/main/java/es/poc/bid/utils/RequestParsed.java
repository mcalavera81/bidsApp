package es.poc.bid.utils;

import es.poc.bid.config.ApplicationPath;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class RequestParsed {


    public RequestParsed(String method, String requestPath,
                         InputStream inputStream){
        setRequestMethod(method);
        setMatchedRequestPath(requestPath);
        readBodyWithErrorHandling(inputStream);
    }

    public HttpMethod matchedHttpMethod;

    private ApplicationPath matchedRequestPath;

    private List<String> requestParams;

    private String bodyContent;

    private OutputStream outputStream;

    public ApplicationPath getMatchedRequestPath() {
        return matchedRequestPath;
    }

    public List<String> getRequestParams() {
        return Collections.unmodifiableList(requestParams);
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public OutputStream getOutputStream() { return outputStream; }

    public void setRequestMethod(String requestMethod) {

        this.matchedHttpMethod = HttpMethod.valueOf(requestMethod);

    }

    public void setMatchedRequestPath(String requestPath){

        for(ApplicationPath applicationPath: ApplicationPath.values()){

            if(isRequestPathMatchingApplicationPath(requestPath,applicationPath)){

                matchedRequestPath = applicationPath;
                requestParams = extractRequestParams(requestPath, applicationPath);
                return;

            }
        }

        throw  new RuntimeException("No matching path for this request " +requestPath);

    }
    private boolean isRequestPathMatchingApplicationPath(String requestPath, ApplicationPath applicationPath){
        return  applicationPath.getHttpMethod() == matchedHttpMethod && applicationPath.getMatcher(requestPath).find();
    }

    private List<String> extractRequestParams(String requestPath, ApplicationPath applicationPath) {
        List<String> params = new ArrayList<>();
        Matcher matcher = applicationPath.getMatcher(requestPath);
        if(matcher.find()){
            for(int i=1; i<= matcher.groupCount();i++){
                params.add(matcher.group(i));
            }
        }
        return params;
    }


    private void readBodyWithErrorHandling(InputStream bodyStream) {


        StringBuilder sb=new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(bodyStream));

        try {
            readBody(sb, br);
        } catch (IOException e) {
            handleErrorReadingBody(e, br);

        }

        bodyContent = sb.toString();
    }

    private void readBody(StringBuilder sb, BufferedReader br) throws IOException {
        String read;
        while((read=br.readLine()) != null) {
            sb.append(read);
        }
    }

    private void handleErrorReadingBody(IOException e, BufferedReader br) {
        try {
            br.close();
        } catch (IOException e1) {
            throw  new RuntimeException("Error closing input stream.");
        }
        throw  new RuntimeException("Error reading body content.");
    }

}
