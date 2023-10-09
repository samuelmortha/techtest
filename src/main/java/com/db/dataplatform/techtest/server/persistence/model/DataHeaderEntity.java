package com.db.dataplatform.techtest.server.persistence.model;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(
        name = "DATA_HEADER",
        uniqueConstraints = @UniqueConstraint(columnNames="NAME")
)
@Setter
@Getter
public class DataHeaderEntity {

    @Id
    @SequenceGenerator(name = "dataHeaderSequenceGenerator", sequenceName = "SEQ_DATA_HEADER", allocationSize = 1)
    @GeneratedValue(generator = "dataHeaderSequenceGenerator")
    @Column(name = "DATA_HEADER_ID")
    private Long dataHeaderId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BLOCKTYPE")
    @Enumerated(EnumType.STRING)
    private BlockTypeEnum blocktype;


    @Column(name = "CREATED_TIMESTAMP")
    private Instant createdTimestamp;

    @PrePersist
    public void setTimestamps() {
        if (createdTimestamp == null) {
            createdTimestamp = Instant.now();
        }
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == this) return true;
    	if(!(o instanceof DataHeaderEntity)) return false;
    	DataHeaderEntity other = (DataHeaderEntity)o;
    	return other.blocktype.equals(this.blocktype)
    			&& other.name.equals(this.name);
    }
    
    @Override
    public int hashCode() {
        int hash = 11;
        hash = 19 * hash + (name == null ? 0 : name.hashCode());
        hash = 19 * hash + (blocktype == null ? 0 : blocktype.hashCode());
        return hash;
    }
}
