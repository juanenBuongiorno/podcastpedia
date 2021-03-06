package org.podcastpedia.web.feeds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.podcastpedia.common.domain.Episode;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Enclosure;
import com.rometools.rome.feed.rss.Item;



public class FoundEpisodesRssFeedView extends AbstractRssFeedView {
	
    protected void buildFeedMetadata(Map model, Channel feed, HttpServletRequest request) {
        feed.setTitle("" + model.get("feed_title"));
        feed.setDescription(" " + model.get("feed_description"));
        feed.setLink("" + model.get("feed_link"));
    }

    protected List buildFeedItems(Map model, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    	
        List<Episode> episodes = (List<Episode>) model.get("list_of_episodes");
        List<Item> items = new ArrayList<Item>(episodes.size());

        for (Episode episode : episodes) {
            Item item = new Item();
            String date = String.format("%1$tY-%1$tm-%1$td", episode.getPublicationDate());
            item.setTitle(episode.getTitle());
            item.setPubDate(episode.getPublicationDate());
            item.setLink(model.get("HOST_AND_PORT_URL") + "/podcasts/" + episode.getPodcastId() 
            		+ "/" + episode.getPodcast().getTitleInUrl()+ "/episodes/" + episode.getEpisodeId()
            		+ "/" + episode.getTitleInUrl());
            
            Description episodeDescription = new Description();
            episodeDescription.setValue(episode.getDescription());
            item.setDescription(episodeDescription);  
            
            //set enclosures
            List<Enclosure> enclosures = new ArrayList<Enclosure>();
            Enclosure enclosure = new Enclosure();
            enclosure.setUrl(episode.getMediaUrl());
            if(episode.getLength() != null) enclosure.setLength(episode.getLength());
            if(episode.getEnclosureType() != null ) enclosure.setType(episode.getEnclosureType()); 
            enclosures.add(enclosure);
            item.setEnclosures(enclosures);
            
            items.add(item);
        }

        return items;
    }
}
