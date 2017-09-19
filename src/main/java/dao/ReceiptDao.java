package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {

        ReceiptsRecord receiptsRecord;
        if (amount == null) {
            receiptsRecord = dsl
                    .insertInto(RECEIPTS, RECEIPTS.MERCHANT)
                    .values(merchantName)
                    .returning(RECEIPTS.ID)
                    .fetchOne();
        } else {
            receiptsRecord = dsl
                    .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                    .values(merchantName, amount)
                    .returning(RECEIPTS.ID)
                    .fetchOne();
        }

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptsRecord> getAllReceipts() {
        List<ReceiptsRecord> receiptResponseList = dsl.selectFrom(RECEIPTS).fetch();

        if (receiptResponseList == null || receiptResponseList.size() < 1) {
            System.out.println("did not find any receipts");
            return new ArrayList<>();

        } else {

            List<TagsRecord> tagsRecordList = dsl.selectFrom(TAGS).fetch();

            if (tagsRecordList == null || tagsRecordList.size() < 1) {
                System.out.println("did not find any tags");
                return receiptResponseList;
            }

            for (TagsRecord record : tagsRecordList) {

                Integer receipt_id = record.getReceiptId();
                for (ReceiptsRecord receiptsRecord : receiptResponseList) {
                    if (receiptsRecord.getId().equals(receipt_id)) {
                        receiptsRecord.getTagList().add(record.getName());
                        break;
                    }
                }
            }

            return receiptResponseList;
        }
    }
}
