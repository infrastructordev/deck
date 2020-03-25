@TypeDefs(
   @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
)
package dev.infrastructr.deck.data.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
