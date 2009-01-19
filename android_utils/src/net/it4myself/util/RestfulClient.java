package net.it4myself.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class RestfulClient {
    private static final String TAG = "Restful";
    public static String basicAuthUsername = "";
    public static String basicAuthPassword = "";

	public static String Get(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpGet method = new HttpGet(fulluri);
		return EntityUtils.toString(DoRequest(method));
	}

	public static Document Get(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpGet method = new HttpGet(fulluri);
		return getDOM(DoRequest(method), builder);
	}

	public static String Post(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		HttpPost method = new HttpPost(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return EntityUtils.toString(DoRequest(method));
	}

	public static Document Post(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		HttpPost method = new HttpPost(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return getDOM(DoRequest(method), builder);
	}

	public static String Put(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		HttpPut method = new HttpPut(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return EntityUtils.toString(DoRequest(method));
	}

	public static Document Put(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		HttpPut method = new HttpPut(uri);
		if(null != map){
			List<NameValuePair> paramList = packEntryParams(map);
			method.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		}
		return getDOM(DoRequest(method), builder);
	}

	public static String Delete(String uri, HashMap<String,String> map) throws ClientProtocolException, IOException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpDelete method = new HttpDelete(fulluri);
		return EntityUtils.toString(DoRequest(method));
	}

	public static Document Delete(String uri, HashMap<String,String> map, DocumentBuilder builder) throws ClientProtocolException, IOException, SAXException {
		String fulluri;

		if(null == map){
			fulluri = uri;
		} else {
			fulluri = uri + packQueryString(map);
		}
		
		HttpDelete method = new HttpDelete(fulluri);
		return getDOM(DoRequest(method), builder);
	}
	
	/*
	 * DocumentBuilderFactory.newInstance()
	 * 	.setValidating(true)
	 * 	.setIgnoringElementContentWhitespace(true)
	 * 	.newDocumentBuilder()
	 *  .parse(hoge);
	 *  でうまく空ノードを取ってくれそうだけど、バリデータが実装されてないのか例外が出る。
	 *  また
	 *  Node.normalize()もなんか変
	 *  なので、自前で改行やスペースだけのテキストノードを削除する。
	 */
    public static Node RemoveEmptyNodes(Node currentNode) {
        NodeList list = currentNode.getChildNodes();
        int n = list.getLength();
        if(0 < n){
            for (int i = 0; i < n; i++) {
                Node childNode = list.item(i);
                String value = childNode.getNodeValue();
                // Log.v(TAG, "value : " + value);
                if(Node.TEXT_NODE == childNode.getNodeType() && value.trim().equals("")){
                	// Log.v(TAG, "remove " + Integer.toString(i) + "th node of " + currentNode.getNodeName());
                	currentNode.removeChild(childNode);
                }else{
                	RemoveEmptyNodes(childNode);
                }
            }
        }
        return currentNode;
    }

	
	private static HttpEntity DoRequest(HttpUriRequest method) throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
		
		// BASIC認証用のユーザ名が設定されていれば、BASIC認証を行う
		if(!basicAuthUsername.equals("")){
			URI uri = method.getURI();
			client.getCredentialsProvider().setCredentials(
				new AuthScope(uri.getHost(), uri.getPort()),
				new UsernamePasswordCredentials(basicAuthUsername, basicAuthPassword));
		}
		HttpResponse response = null;
		
		try {
			response = client.execute(method);
			int statuscode = response.getStatusLine().getStatusCode();
			
			//リクエストが成功 200 OK and 201 CREATED
			if (statuscode == HttpStatus.SC_OK | statuscode == HttpStatus.SC_CREATED){ 
				return response.getEntity();
			} else {
				throw new HttpResponseException(statuscode, "Response code is " + Integer.toString(statuscode));
			}
		}catch (RuntimeException e) {
			method.abort();
			Log.v(TAG, e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	private static List<NameValuePair> packEntryParams(HashMap<String,String> map){
		if(null == map){
			throw new RuntimeException("map is null");
		}

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
		Map.Entry<String, String> entry;
		
		while(itr.hasNext()){
			entry = itr.next();
			paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return paramList;
	}
	
	private static String packQueryString(HashMap<String,String> map) throws UnsupportedEncodingException{
		if(null == map){
			throw new RuntimeException("map is null");
		}
		
		StringBuilder sb = new StringBuilder(100);
		Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
		Map.Entry<String, String> entry;
		
		while(itr.hasNext()){
			entry = itr.next();
			if(0 == sb.length()){
				sb.append("?");
			}else{
				sb.append("&");
			}
			sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			sb.append("=");
			sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		return sb.toString();
	}
	
	private static Document getDOM(HttpEntity entity, DocumentBuilder builder) throws IOException, SAXException{
		BufferedInputStream is = new BufferedInputStream(entity.getContent());
		Document doc = null;
		try {
			doc = builder.parse(is);
			return doc;
		} finally{
			is.close();
		}
	}
}
