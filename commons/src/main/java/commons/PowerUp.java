package commons;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


@Entity
@Table(name = "POWER_UPS_USED")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING,
        name = "POWER_UP_TYPE")
public abstract class PowerUp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name="USERNAME")
    public String username;

    @Column(name="TIME")
    public String time;

    public PowerUp(){
        // for object mappers
    }



    public PowerUp(String username,String time) {
        this.username = username;
        this.time = time;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
