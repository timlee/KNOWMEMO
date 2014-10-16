package services;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.SearchParameters;



public class PictureSearch extends Activity {
	
	 String apiKey = "de2336eadf4f213c6efdacdb8180a8eb";
	 public static final String API_SEC = "5fcb6a6f375f6804"; 
	 private static final Logger logger = LoggerFactory.getLogger(PictureSearch.class);
	 	
	 private Flickr flickr  = new Flickr(apiKey, API_SEC);
	 

	 public Flickr getFlickr() {
		return flickr;
	}

	 


	public void setFlickr(Flickr flickr) {
		this.flickr = flickr;
	}

	@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.activity_main);			 
			 logger.debug("Flickr object = "+ flickr.toString());
			 
			

		}
	 
	 public void initialOauth() {
		   //String callBackUrl = "your_scheme_defined_in_androidmanifest.xml";
		  // Flickr f = new Flickr("api_key", "api_secret");
		   //get a request token from Flickr
//		   OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(callBackUrl);
//		   //you should save the request token and token secret to a preference store for later use.
//		   saveToken(oauthToken);
//		 
//		  //build the Authentication URL with the required permission
//		   URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
//		                                         Permission.WRITE, oauthToken);
		 

		  //redirect user to the genreated URL.
		   //redirect(oauthUrl);
		 }
	 
	 public ArrayList<String> SearchPhoto(String word){
		 		 
		 SearchParameters params = new SearchParameters();
		 params.setText(word);
		 params.setAccuracy(Flickr.ACCURACY_WORLD);
		 params.setSort(SearchParameters.RELEVANCE);
		 params.setSafeSearch(Flickr.SAFETYLEVEL_RESTRICTED);
		 //params.setLicense(license);
		 PhotoList photoList;
		 Photo photo;
		 ArrayList<String> photoURLArray = new ArrayList<String>();
		try {
			photoList = flickr.getPhotosInterface().search(params, 10, 1);
			 for (int i = 0 ; i< photoList.size(); i++){
				 photo = photoList.get(i);
				 String photoURL = photo.getSmallUrl();
				 photoURLArray.add(photoURL);
			}
			 return photoURLArray;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
	 }
	 
	 public PhotoList SearchFlickrPhoto(String word){
 		 
		 SearchParameters params = new SearchParameters();
		 params.setText(word);
		 params.setAccuracy(Flickr.ACCURACY_WORLD);
		 params.setSort(SearchParameters.RELEVANCE);
		 params.setSafeSearch(Flickr.SAFETYLEVEL_SAFE);
		 params.setLicense("4");
		 PhotoList photoList;
		 Photo photo;
		 ArrayList<String> photoURLArray = new ArrayList<String>();
		try {
			photoList = flickr.getPhotosInterface().search(params, 20, 1);
			

			 return photoList;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
	 }
}
