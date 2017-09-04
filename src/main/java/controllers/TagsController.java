package controllers;

import api.ReceiptResponse;
import dao.TagsDao;
import generated.tables.records.ReceiptsRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagsController extends NoContentToOkResposeFilter{
    final TagsDao tagsDao;

    public TagsController(TagsDao tagsDao) {
        this.tagsDao = tagsDao;
    }

    @Path("/{tag}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateTagEntry(@PathParam("tag") String tagName, Integer receipt_id ) {
        tagsDao.insert(tagName, receipt_id);
    }

    @Path("/{tag}")
    @GET
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = tagsDao.getTaggedReceipts(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}
