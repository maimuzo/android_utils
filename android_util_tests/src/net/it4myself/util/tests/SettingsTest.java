package net.it4myself.util.tests;


import java.util.List;

import net.it4myself.util.Settings;
import android.test.AndroidTestCase;
/*
 * adb shell am instrument -w net.it4myself.hyperonigokko.tests/android.test.InstrumentationTestRunner 
 */
public class SettingsTest extends AndroidTestCase {
	Settings settings = null;
	
    protected void setUp() {
    	settings = new Settings(getContext(), "unitTestDb.db");
    	clearData();
    }

    public void testShouldCreateNewDB() {
    	Settings settingsNew = new Settings(getContext(), "unitTestDbNewCreate.db");
        assertEquals(Settings.class, settingsNew.getClass());
    }
    
    public void testShouldCreateSeed() {
    	Settings settingsNew = new Settings(getContext(), "unitTestDbNewSeed.db");
    	assertEquals(1, settingsNew.keysInDB().size());
    	assertTrue(settingsNew.hasKeyInMap("seed"));
    	assertTrue(settingsNew.hasKeyInDB("seed"));
    	
    	assertTrue(0 < settingsNew.get("seed").length());
    }

    public void testShouldBeAbleToSetAndGet() {
    	String key = "test1";
    	String value = "value1";
    	
    	settings.set(key, value);
    	
    	assertTrue(settings.hasKeyInDB(key));
    	assertTrue(settings.hasKeyInMap(key));
    	assertEquals(value, settings.get(key));
    }

    public void testShouldHaveRestoredData(){
    	String key = "testRestore";
    	String value = "valueRestore";
    	
    	settings.set(key, value);
    	settings = null;
    	Settings settingsRestored = new Settings(getContext(), "unitTestDb.db");
    	
    	assertTrue(settingsRestored.hasKeyInDB(key));
    	assertTrue(settingsRestored.hasKeyInMap(key));
    	assertEquals(value, settingsRestored.get(key));
    }
    
    public void testShouldUpdateKeys(){
    	String key = "testUpdate";
    	String value1 = "valueUpdate1";
    	String value2 = "valueUpdate2";
    	
    	settings.set(key, value1);
    	settings.set(key, value2);
    	
    	assertTrue(settings.hasKeyInDB(key));
    	assertTrue(settings.hasKeyInMap(key));
    	assertNotSame(value1, settings.get(key));
    	assertEquals(value2, settings.get(key));
    }
    
    public void testSholdDeleteKeys(){
    	String key = "testDelete";
    	String value = "valueDelete";
    	
    	settings.set(key, value);
    	settings.delete(key);
    	
    	assertFalse(settings.hasKeyInDB(key));
    	assertFalse(settings.hasKeyInMap(key));
    	assertEquals(null, settings.get(key));
    }
    
    private void clearData(){
    	List<String> keys = settings.keysInDB();
    	for(String key:keys){
    		if("seed" != key)
    			settings.delete(key);
    	}
    }
}
