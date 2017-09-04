package api;


import dao.TagsDao;
import io.dropwizard.jersey.validation.Validators;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class CreateReceiptRequestTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testValid() {
        CreateReceiptRequest receipt = new CreateReceiptRequest();
        receipt.merchant = "OK";

        receipt.amount = new BigDecimal(33.44);
        assertThat(validator.validate(receipt), empty());
    }

    @Test
    public void testMissingAmount() {
        CreateReceiptRequest receipt = new CreateReceiptRequest();
        receipt.merchant = "OK";

        //receipt.amount = new BigDecimal(33.44);
        assertThat(validator.validate(receipt), empty());
    }

    @Test
    public void testMissingMerchant() {
        CreateReceiptRequest receipt = new CreateReceiptRequest();
        receipt.amount = new BigDecimal(33.44);

        validator.validate(receipt);
        assertThat(validator.validate(receipt), hasSize(1));
    }

    @Test
    public void testInsertion() {
        // Create Mock Object
        TagsDao tagsDao = createMockDao();

        // Test Insertion
        Integer value = tagsDao.insert("tagName", 1);
        assert(value.equals(1));

        // Test removal
        value = tagsDao.insert("tagName", 1);
        assert(value.equals(-1));

    }

    private TagsDao createMockDao() {
        return new TagsDao(setupJooq());
    }

    // Code copied for Test purpose. Making it external was harder.
    private static org.jooq.Configuration setupJooq() {
        // For now we are just going to use an H2 Database.  We'll upgrade to mysql later
        // This connection string tells H2 to initialize itself with our schema.sql before allowing connections
        final String jdbcUrl = "jdbc:h2:mem:test;MODE=MySQL;INIT=RUNSCRIPT from 'classpath:schema.sql'";
        JdbcConnectionPool cp = JdbcConnectionPool.create(jdbcUrl, "sa", "sa");

        // This sets up jooq to talk to whatever database we are using.
        org.jooq.Configuration jooqConfig = new DefaultConfiguration();
        jooqConfig.set(SQLDialect.MYSQL);   // Lets stick to using MySQL (H2 is OK with this!)
        jooqConfig.set(cp);
        return jooqConfig;
    }
}