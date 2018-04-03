// package io.openliberty.guides.service;
//
// import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
// import javax.ws.rs.ext.Provider;
// import javax.ws.rs.core.MultivaluedMap;
// import javax.ws.rs.core.Response;
//
// @Provider
// public class PlaylistResponseExceptionMapper implements
//     ResponseExceptionMapper<BasePlaylistException> {
//
//     @Override
//     public boolean handles(int statusCode, MultivaluedMap<String, Object> headers) {
//         return statusCode == 404  // Not Found
//             || statusCode == 409; // Conflict
//     }
//
//     @Override
//     public BasePlaylistException toThrowable(Response response) {
//         switch(response.getStatus()) {
//         case 404: return new UnknownPlaylistException();
//         case 409: return new PlaylistAlreadyExistsException();
//         }
//         return null;
//     }
//
// }
