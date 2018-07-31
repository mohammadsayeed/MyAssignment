package com.klm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@RestController
@RequestMapping("/api/{userId}")
public class FeedChickenAndCollectEggController {
	
		
	
	@GetMapping(path="/collect_after_feed", produces="application/json")

	public @ResponseBody ChikenEggResult CollectAfterFeed (@PathVariable("userId") String userId)
	{
	
		String chickens_feed_service = "http://coop.apps.knpuniversity.com/api/"+userId+"/chickens-feed";
		
		String eggs_collect_service = "http://coop.apps.knpuniversity.com/api/"+userId+"/eggs-collect";

		String token = "18280b7b18500283d185c7afed6877049c627bc2"; // Need to generete through coop.aaps.university
		ChikenEggResult  klm = new ChikenEggResult();
		
		Client client1= Client.create();
		WebResource wr = client1.resource(chickens_feed_service);
		ClientResponse clientResponse1 = wr.accept("application/json").header("Authorization", "Bearer "+token).post(ClientResponse.class);
		int feedChickenResponseCode =clientResponse1.getClientResponseStatus().getStatusCode();
		
		// client call for eggs collect service
		
		Client client2= Client.create();
		WebResource wr2= client2.resource(eggs_collect_service);
		ClientResponse clientResponse = wr2.accept("application/json").header("Authorization", "Bearer "+token).post(ClientResponse.class);

		int collectEggsResponseCode = clientResponse.getClientResponseStatus().getStatusCode();

		if((feedChickenResponseCode == 200)&&(collectEggsResponseCode == 200))
		{
			klm = new ChikenEggResult();
			klm.setCollect("success");
			klm.setFeed("success");
		}
		else if((feedChickenResponseCode == 200)&&(collectEggsResponseCode == 202))
		{
			klm = new ChikenEggResult();
			klm.setCollect("success");
			klm.setFeed("failed");
		}
		else if((feedChickenResponseCode == 202)&&(collectEggsResponseCode == 200))
		{
			klm = new ChikenEggResult();
			klm.setCollect("failed");
			klm.setFeed("success");
		}
		else if((feedChickenResponseCode == 400)&&(collectEggsResponseCode == 400))
		{
			klm = new ChikenEggResult();
			klm.setCollect("failed");
			klm.setFeed("failed");
		}

		return klm;


	}


}
