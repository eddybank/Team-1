package fleaMarket;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class StatisticAnalysis {

	/*
	 * We will either use txt files or XML files to store record for individual users
	 * Upon clicking on "Check Game Stats" we will ask for a username and then find the user within the file
	 */
	//private static String user = SolitaireMenu.getUser();
	
	private static void setUserColor(User user) 
	{
		
	}
	
	public static String getUserColor(User user) 
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		String color = "";
	    try 
	    {
	    	 documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    	 Document document;
	    	 document = documentBuilder.parse("SolitaireFileFormat.xml");

	    	 NodeList users = document.getElementsByTagName("user");
	    	 
	    	 int time = 0;
	    	 int score = 0;
	    	 
	    	 for(int i = 0; i < users.getLength(); i++)
	    	 {
	    		 Node node = users.item(i);
	    		 Element ele = (Element) node;
	    		 if(user.getUser().equals(ele.getElementsByTagName("name").item(0).getTextContent())) {
	    			 color = ele.getElementsByTagName("bgcolor").item(0).getTextContent();
	    		 }
	    	 }
		} catch (SAXException | IOException | ParserConfigurationException | NullPointerException e) 
	     {
			System.out.println("Error occurred - Printing stack trace");
			e.printStackTrace();
		 }
	    return color;
	}
	
	public static boolean doesUserExist(String name) 
	{
		ArrayList<User> allUsers = getAllUsers();
		boolean doesExist = false;
		for( User u : allUsers) {
			if(u.getUser().equals(name))
			{
				doesExist = true;
			} else {
				doesExist = false;
			}
		}
		return doesExist;
	}
	
	private static ArrayList<User> getAllUsers() 
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		ArrayList<User> allUsers = new ArrayList<User>();
		Record rec = new Record();
		User user = new User();
	    try 
	    {
	    	 documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    	 Document document;
	    	 document = documentBuilder.parse("SolitaireFileFormat.xml");

	    	 NodeList users = document.getElementsByTagName("user");
	    	 
	    	 int time = 0;
	    	 int score = 0;
	    	 
	    	 for(int i = 0; i < users.getLength(); i++)
	    	 {
	    		 Node node = users.item(i);
	    		 Element ele = (Element) node;
	    		 user = new User(ele.getElementsByTagName("name").item(0).getTextContent());
	    		 user.setBestTime(Integer.parseInt(ele.getElementsByTagName("best_time").item(0).getTextContent()));
	    			 if(node.getNodeType() == Node.ELEMENT_NODE) 
	    			 {
	    				 NodeList recs = ele.getElementsByTagName("record");
	    				 
	    				 for(int y = 0; y < recs.getLength(); y++)
	    				 {
		    				 time = Integer.parseInt(ele.getElementsByTagName("score").item(y).getTextContent());
		    				 score = Integer.parseInt(ele.getElementsByTagName("time").item(y).getTextContent());
		    				 rec = new Record(time, score);
		    				 user.addRecord(rec);
		    				//System.out.println(rec);
	    				 }
	    			 }
	    			 allUsers.add(user);
	    	 }
		} catch (SAXException | IOException | ParserConfigurationException e) 
	     {
			System.out.println("Error occurred - Printing stack trace");
			e.printStackTrace();
		 }
		return allUsers;
	}	
	public static User setUser(String name) 
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		Record rec = new Record();
		User user = new User();
	    try 
	    {
	    	 documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    	 Document document;
	    	 document = documentBuilder.parse("SolitaireFileFormat.xml");

	    	 NodeList users = document.getElementsByTagName("user");
	    	 
	    	 int time = 0;
	    	 int score = 0;
	    	 
	    	 for(int i = 0; i < users.getLength(); i++)
	    	 {
	    		 Node node = users.item(i);
	    		 Element ele = (Element) node;
	    		 if(name.equals(ele.getElementsByTagName("name").item(0).getTextContent())) 
	    		 {
	    			 user = new User(ele.getElementsByTagName("name").item(0).getTextContent());
	    			 user.setBestTime(Integer.parseInt(ele.getElementsByTagName("best_time").item(0).getTextContent()));
	    			 if(node.getNodeType() == Node.ELEMENT_NODE) 
	    			 {
	    				 NodeList recs = ele.getElementsByTagName("record");
	    				 
	    				 for(int y = 0; y < recs.getLength(); y++)
	    				 {
		    				 time = Integer.parseInt(ele.getElementsByTagName("score").item(y).getTextContent());
		    				 score = Integer.parseInt(ele.getElementsByTagName("time").item(y).getTextContent());
		    				 rec = new Record(time, score);
		    				 user.addRecord(rec);
	    				 }
	    			 }
	    		 }
	    	 }
		} catch (SAXException | IOException | ParserConfigurationException e) 
	     {
			System.out.println("Error occurred - Printing stack trace");
			e.printStackTrace();
		 }
	  System.out.println(user);
	    return user;
	}	
	
	public static ArrayList<Record> getRecords(User user) 
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Record rec = new Record();
	    try 
	    {
	    	 documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    	 Document document;
	    	 document = documentBuilder.parse("SolitaireFileFormat.xml");

	    	 NodeList users = document.getElementsByTagName("user");
	    	 
	    	 int best_time = 0;
	    	 int time = 0;
	    	 int score = 0;
	    	 
	    	 for(int i = 0; i < users.getLength(); i++)
	    	 {
	    		 Node node = users.item(i);
	    		 Element ele = (Element) node;
	    		 //System.out.println(ele.getElementsByTagName("name").item(0).getTextContent());
	    		 //System.out.println(user.getUser());
	    		 if(user.getUser().equals(ele.getElementsByTagName("name").item(0).getTextContent())) 
	    		 {
	    			 if(node.getNodeType() == Node.ELEMENT_NODE) 
	    			 {
	    				 best_time = Integer.parseInt(ele.getElementsByTagName("best_time").item(0).getTextContent());
	    				 
	    				 NodeList recs = ele.getElementsByTagName("record");
	    				 
	    				 for(int y = 0; y < recs.getLength(); y++)
	    				 {
		    				 time = Integer.parseInt(ele.getElementsByTagName("score").item(y).getTextContent());
		    				 score = Integer.parseInt(ele.getElementsByTagName("time").item(y).getTextContent());
		    				 rec = new Record(time, score);
		    				 user.addRecord(rec);
		    				System.out.println(rec);
	    				 }
	    			 } 
	    		 }
	    	 }
		} catch (SAXException | IOException | ParserConfigurationException e) 
	     {
			System.out.println("Error occurred - Printing stack trace");
			e.printStackTrace();
		 }
		return user.getRecords();
	}	
	
	private static void setRecord(User user, Record r) 
	{
		 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder documentBuilder;
	     try 
	     {
	    	 documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    	 Document document;
	    	 document = documentBuilder.parse("SolitaireFileFormat.xml");
				
	    	 Element root = document.getDocumentElement();
	    	 
	    	 
	    	 
	    	 System.out.println(root.getElementsByTagName("user").item(0));
	    	 // user elements
	    	 NodeList users = document.getElementsByTagName("user");
	    	 System.out.println(users.item(0));
	    	 
	    	for(int i = 0; i < users.getLength(); i++)
	    	 {
	    		 Node node = users.item(i);
	    		 Element ele = (Element) node;
	    		 //System.out.println(ele.getElementsByTagName("name").item(0).getTextContent());
	    		 //System.out.println(user.getUser());
	    		 if(user.getUser().equals(ele.getElementsByTagName("name").item(0).getTextContent())) 
	    		 {
	    			 ele.getElementsByTagName("best_time").item(0).setTextContent(Integer.toString(user.getBestTime()));
	    			 
	    			 Element record = document.createElement("record");

	    			 Element score = document.createElement("score");
	    			 score.appendChild(document.createTextNode(Integer.toString(r.getScore())));
	    			 record.appendChild(score);
	    	        
	    			 Element time = document.createElement("time");
	    			 time.appendChild(document.createTextNode(Integer.toString(r.getTime())));
	    			 record.appendChild(time);
	    	        
	    			 ele.appendChild(record);
	    		 }
	    	 }
	        
	    	 DOMSource source = new DOMSource(document);

	    	 TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    	 Transformer transformer = transformerFactory.newTransformer();
	    	 //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    	 StreamResult result = new StreamResult("SolitaireFileFormat.xml");
	    	 transformer.transform(source, result);
	        
	        
			} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) // 
	     	{
				System.out.println("Error occurred - Printing stack trace");
				e.printStackTrace();
			}
	}	
	
	private static void newUser(User user) 
	{
		 DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder documentBuilder;
		 
		 try 
		 {
			 documentBuilder = documentBuilderFactory.newDocumentBuilder();
			 Document document;
			 document = documentBuilder.parse("SolitaireFileFormat.xml");
			
			 Element root = document.getDocumentElement(); 
	    	 
	    	 if(!doesUserExist(user.getUser())) {
		    	 Element newUser = document.createElement("user");
	
				 Element name = document.createElement("name");
				 name.appendChild(document.createTextNode(user.getUser()));
				 newUser.appendChild(name);
		        
				 Element bestTime = document.createElement("best_time");
				 bestTime.appendChild(document.createTextNode(Integer.toString(user.getBestTime())));
				 newUser.appendChild(bestTime);
				 
				 Element color = document.createElement("bgcolor");
				 color.appendChild(document.createTextNode(SolitaireMenu.getColor().toString()));
				 newUser.appendChild(color);
				 
				 root.appendChild(newUser);
	    	 }

			 DOMSource source = new DOMSource(document);

			 TransformerFactory transformerFactory = TransformerFactory.newInstance();
			 Transformer transformer = transformerFactory.newTransformer();
			 //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			 StreamResult result = new StreamResult("SolitaireFileFormat.xml");
			 transformer.transform(source, result);
	        
			} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) 
		 	{
				System.out.println("Error occurred - Printing stack trace");
				e.printStackTrace();
			}
	}
	
	public static class User 
	{
		
		private String username;
		private int best_time;
		
		private ArrayList<Record> records = new ArrayList<Record>();
		
		User(){}
		User(String user)
		{
			username = user;
			best_time = 0;
		}
		
		User(String user, int bt)
		{
			username = user;
			best_time = bt;
			newUser(this);
		}
		
		String getUser() 
		{
			return username;
		}
		
		int getBestTime() 
		{
			return best_time;
		}
		void setBestTime()
		{
			int curBestTime = 0;
			if(records.size() > 1)
			{
				for(int x = 0; x < records.size() - 1; x++)
				{
					if(records.get(x).getTime() > records.get((x+1)).getTime())
					{
						curBestTime = records.get(x+1).getTime();
					} else 
					{
						curBestTime = records.get(x).getTime();
					}
				}
			} else
			{
				curBestTime = records.get(records.size()-1).getTime();
			}
			best_time = curBestTime;
		}
		
		void setBestTime(int t)
		{
			best_time = t;
		}
		
		boolean isBestTime(Record r)
		{
			if(r.getTime() >= getBestTime())
			{
				return true;
			} else 
			{
				return false;
			}
		}
		
		void addRecord(Record rec) 
		{
			records.add(rec);
		}

		void createRecord(int score, int time) 
		{
			Record newR = new Record(score, time);
			records.add(newR);
			setBestTime();
			setRecord(this, newR);
		}
		
		ArrayList<Record> getRecords()
		{
			return records;
		}
		public String toString()
		{
			String userStr = "Username: "+this.getUser()+" Best Time: "+this.getBestTime()+"\n";
			for(Record r : this.getRecords()) 
			{
				userStr += "	"+r.toString()+"\n";
			}
			return userStr;
		}
	}
	
	private static class Record 
	{
		private int time;
		private int score;
			
		Record(){}
		
		Record(int s, int t){
			score = s;
			time = t;
		}
				
		int getScore()
		{
			return score;
		}
			
		int getTime()
		{
			return time;
		}
		public String toString()
		{
			return "Time: "+time+", Score: "+score;
		}
	}

	
		
	
	public static void main(String[] args) {
		
		User test1 = setUser("test");
		System.out.println(getUserColor(test1));
		
		//test1.createRecord(416, 1102);
		
		
		//User test2 = new User("bobby", 0);
		//User test3 = new User("tommy", 0);
		
		//test3.createRecord(416, 999);
		
		//test1.createRecord(416, 1021);
		
		//System.out.println((getRecords(test1)));
		//System.out.println((getAllUsers()));
		//System.out.println(test1.getBestTime());
	}
	
}
