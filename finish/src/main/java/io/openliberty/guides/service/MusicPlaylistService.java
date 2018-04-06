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
import io.openliberty.guides.service.PlaylistResponseExceptionMapper;

// @Path("/playlist")
// @Consumes(MediaType.APPLICATION_JSON)
// @Dependent
// @RegisterRestClient
// public interface MusicPlaylistService {
//
//     @GET
//     public List<String> getPlaylistNames();
//
//     @GET
//     @Path("/{playlistName}")
//     public String getPlaylist(@PathParam("playlistName") String name);
//         // throws UnknownPlaylistException;
//
//     @POST
//     @Path("/{playlistName}")
//     public long newPlayList(@PathParam("playlistName") String name);
//         // throws PlaylistAlreadyExistsException;
//
// }

// @RegisterProvider(PlaylistResponseExceptionMapper.class)

@Path("/playlist")
@Consumes("application/json")
public interface MusicPlaylistService {

    @GET
    List<String> getPlaylistNames();

    @GET
    @Path("/{playlistName}")
    List<Song> getPlaylist(@PathParam("playlistName") name)
        throws UnknownPlaylistException;

    @POST
    @Path("/{playlistName}")
    long newPlayList(@PathParam("playlistName") name, List<Song> playlist)
        throws PlaylistAlreadyExistsException;

    @PUT
    @Path("/{playlistName}")
    long updatePlayList(@PathParam("playlistName") name, List<Song> playlist)
        throws UnknownPlaylistException;
}
