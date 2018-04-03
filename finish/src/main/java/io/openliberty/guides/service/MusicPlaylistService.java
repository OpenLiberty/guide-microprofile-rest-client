package io.openliberty.guides.service;

import java.util.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.enterprise.context.Dependent;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
// import io.openliberty.guides.service.PlaylistResponseExceptionMapper;

@Path("/playlist")
@Consumes(MediaType.APPLICATION_JSON)
@Dependent
@RegisterRestClient
public class MusicPlaylistService {

    @GET
    public String getPlaylistNames() {
      return "lalal";
    }


    @GET
    @Path("/{playlistName}")
    public String getPlaylist(@PathParam("playlistName") String name){
      return "lalal";
    }
        // throws UnknownPlaylistException;

    @POST
    @Path("/{playlistName}")
    public void newPlayList(@PathParam("playlistName") String name, String playlist){
    }
        // throws PlaylistAlreadyExistsException;

}

// @RegisterProvider(PlaylistResponseExceptionMapper.class)
