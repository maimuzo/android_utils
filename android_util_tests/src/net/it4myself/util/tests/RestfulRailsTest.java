package net.it4myself.util.tests;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.it4myself.util.RestfulRails;
import android.test.AndroidTestCase;
import android.util.Log;

/*
 * adb shell am instrument -w net.it4myself.hyperonigokko.tests/android.test.InstrumentationTestRunner 
 */

public class RestfulRailsTest extends AndroidTestCase {
	private static String HOST = "http://192.168.10.181:3000"; // TODO: replace to your test server's IP
	private static String TAG = "RestfulTest";
	private DocumentBuilder builder;
	
    protected void setUp() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//factory.setNamespaceAware(true);
		
		try {
			builder = factory.newDocumentBuilder();
			deleteAll();
		} catch (ParserConfigurationException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		
    }

    public void testShouldPostAndGetString(){
    	HashMap<String,String> params = new HashMap<String,String>();
    	params.put("user[key]", "11");
    	params.put("user[name]", "postTestForString");
    	params.put("from", "unittest"); // this is a marker. not use.
    	try {
			String result = RestfulRails.Post(HOST + "/users.xml", params);
			Log.v(TAG, result);
			assertTrue(null != result);
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldPostAndGetDOM(){
    	HashMap<String,String> params = new HashMap<String,String>();
    	String userKey = "12";
    	params.put("user[key]", userKey);
    	params.put("user[name]", "postTestForDOM");
    	params.put("from", "unittest"); // this is a marker. not use.
    	boolean foundKey = false;
    	try {
    		Document result = RestfulRails.Post(HOST + "/users.xml", params, builder);
			Log.v(TAG, result.toString());
    		NodeList list = result.getDocumentElement().getChildNodes();
    		Node node;
    		for (int i=0; null != (node = list.item(i)); i++) {
    			if(node.getNodeName().equals("key")){
    				assertEquals(userKey, node.getFirstChild().getNodeValue());
    				foundKey = true;
    			}
    		}
			assertTrue(foundKey);
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldGetListAndGetString() {
    	try {
    		String result = RestfulRails.Get(HOST + "/users.xml", null);
    		Log.v(TAG, result);
    		assertTrue(null != result);
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldGetListAndGetDOM() {
    	try {
    		deleteAll();
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("user[key]", "31");
        	params.put("user[name]", "forList1");
        	params.put("from", "unittest"); // this is a marker. not use.
        	RestfulRails.Post(HOST + "/users.xml", params);
        	params.put("user[key]", "32");
        	params.put("user[name]", "forList2");
        	RestfulRails.Post(HOST + "/users.xml", params);
        	params.put("user[key]", "33");
        	params.put("user[name]", "forList3");
        	RestfulRails.Post(HOST + "/users.xml", params);

        	Document result = RestfulRails.Get(HOST + "/users.xml", null, builder);
    		NodeList list = result.getDocumentElement().getChildNodes();
    		Node node1;
    		for (int i=0; null != (node1 = list.item(i)); i++) {
    			Log.v(TAG, node1.getNodeName());
    		}

    		assertEquals(3, list.getLength());
    		Node target = list.item(0);
    		assertEquals("user", target.getNodeName());
    		assertEquals("31", target.getChildNodes().item(1).getFirstChild().getNodeValue());
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldGetRecoadAndGetString() {
    	try {
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("user[key]", "41");
        	params.put("user[name]", "forGetRecord1");
        	params.put("from", "unittest"); // this is a marker. not use.
        	String postedId = getIdString(RestfulRails.Post(HOST + "/users.xml", params, builder));

    		String result = RestfulRails.Get(HOST + "/users/" + postedId + ".xml", null);
			Log.v(TAG, result);
    		assertTrue(null != result);
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldGetRecoadAndGetDOM() {
    	try {
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("user[key]", "51");
        	params.put("user[name]", "forGetRecord1");
        	params.put("from", "unittest"); // this is a marker. not use.
        	String postedId = getIdString(RestfulRails.Post(HOST + "/users.xml", params, builder));

        	Document result = RestfulRails.Get(HOST + "/users/" + postedId + ".xml", null, builder);
    		Log.v(TAG, result.toString());
    		
    		NodeList list = result.getDocumentElement().getChildNodes();
    		assertEquals(1, list.getLength());
    		Node target = list.item(0);
    		assertEquals("id", target.getNodeName());
    		assertEquals(postedId, target.getFirstChild().getNodeValue());
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }
    
    public void testShouldPutAndGetString(){
    	try {
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("user[key]", "61");
        	params.put("user[name]", "forPutRecord1");
        	params.put("from", "unittest"); // this is a marker. not use.
        	String postedId = getIdString(RestfulRails.Post(HOST + "/users.xml", params, builder));

        	params.put("user[id]", postedId);

        	String putId = getIdString(RestfulRails.Put(HOST + "/users/" + postedId + ".xml", params, builder));
        	
    		String result = RestfulRails.Get(HOST + "/users/" + putId + ".xml", params);
			Log.v(TAG, result);
    		assertTrue(null != result);
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldPutAndGetDOM(){
    	try {
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("user[key]", "61");
        	params.put("user[name]", "forPutRecord1");
        	params.put("from", "unittest"); // this is a marker. not use.
        	String postedId = getIdString(RestfulRails.Post(HOST + "/users.xml", params, builder));

        	params.put("user[id]", postedId);
        	params.put("user[key]", "62");
        	params.put("user[name]", "forPutRecord1Modified");

        	String putId = getIdString(RestfulRails.Put(HOST + "/users/" + postedId + ".xml", params, builder));
        	
        	Document result = RestfulRails.Get(HOST + "/users/" + putId + ".xml", null, builder);
    		Log.v(TAG, result.toString());
    		
    		NodeList list = result.getDocumentElement().getChildNodes();
    		assertEquals(1, list.getLength());
    		Node target = list.item(0);
    		assertEquals("id", target.getNodeName());
    		assertEquals(putId, target.getFirstChild().getNodeValue());
    		assertEquals("62", list.item(1).getFirstChild().getNodeValue());
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }

    public void testShouldDeleteAll(){
    	try {
        	HashMap<String,String> params = new HashMap<String,String>();
        	params.put("user[key]", "1");
        	params.put("user[name]", "fordelete1");
        	params.put("from", "unittest"); // this is a marker. not use.
        	RestfulRails.Post(HOST + "/users.xml", params);
        	params.put("user[key]", "2");
        	params.put("user[name]", "fordelete2");
        	RestfulRails.Post(HOST + "/users.xml", params);
        	params.put("user[key]", "3");
        	params.put("user[name]", "fordelete3");
        	RestfulRails.Post(HOST + "/users.xml", params);
    		
        	deleteAll();

    		Document afterRecord = RestfulRails.Get(HOST + "/users.xml", null, builder);
    		if(afterRecord.getDocumentElement().hasChildNodes()){
    			assertTrue(false);
    		}else{
    			assertTrue(true);
    		}
			return;
		} catch (ClientProtocolException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			Log.v(TAG, e.getMessage());
			e.printStackTrace();
		}
		assertTrue(false);
    }
    
    private void deleteAll() throws ClientProtocolException, IOException, SAXException{
		Document allRecord = RestfulRails.Get(HOST + "/users.xml", null, builder);
		NodeList list = allRecord.getDocumentElement().getChildNodes();
		Node node1;
		for (int i=0; null != (node1 = list.item(i)); i++) {
			if(node1.getNodeName().equals("user")){
	    		NodeList userColumns = node1.getChildNodes();
	    		Node node2;
	    		for (int ii=0; null != (node2 = userColumns.item(ii)); ii++) {
	    			if(node2.getNodeName().equals("id")){
	    				RestfulRails.Delete(HOST + "/users/" + node2.getFirstChild().getNodeValue() + ".xml", null);
	    				break;
	    			}
	    		}
			}
		}
    }
    
    private String getIdString(Document postedRecord){
		Log.v(TAG, "in getIdString:" + postedRecord.toString());
		NodeList postedList = postedRecord.getDocumentElement().getChildNodes();
		Node postedNode;
		String postedId = "";
		for (int i=0; null != (postedNode = postedList.item(i)); i++) {
			if(postedNode.getNodeName().equals("id")){
				postedId =  postedNode.getFirstChild().getNodeValue();
				break;
			}
		}
		Log.v(TAG, "postedId:" + postedId);
    	return postedId;
    }
}
