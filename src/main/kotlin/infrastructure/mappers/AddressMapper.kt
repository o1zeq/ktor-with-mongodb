package top.dedicado.infrastructure.mappers

import org.bson.Document
import top.dedicado.domain.models.Address

object AddressMapper {

    fun toDocument(fields: Address): Document {

        return Document().apply {
            fields.kind?.let { put(fields::kind.name, it) }
            fields.street?.let { put(fields::street.name, it) }
            fields.number?.let { put(fields::number.name, it) }
            fields.complement?.let { put(fields::complement.name, it) }
            fields.district?.let { put(fields::district.name, it) }
            fields.city?.let { put(fields::city.name, it) }
            fields.state?.let { put(fields::state.name, it) }
            fields.country?.let { put(fields::country.name, it) }
            fields.coordinate?.let { put(fields::coordinate.name, CoordinateMapper.toDocument(it)) }
        }
    }

    fun toMap(fields: Address): Map<String,Any?> {
        val update = mutableMapOf<String, Any?>()

        fields.kind?.let { update[fields::kind.name] = it }
        fields.zipcode?.let { update[fields::zipcode.name] = it }
        fields.street?.let { update[fields::street.name] = it }
        fields.number?.let { update[fields::number.name] = it }
        fields.complement?.let { update[fields::complement.name] = it }
        fields.district?.let { update[fields::district.name] = it }
        fields.city?.let { update[fields::city.name] = it }
        fields.state?.let { update[fields::state.name] = it }
        fields.country?.let { update[fields::country.name] = it }
        fields.coordinate?.let { update[fields::coordinate.name] = CoordinateMapper.toMap(it) }

        return update.filterValues { it != null }
    }

    fun toModel(document: Document): Address {
        return Address(
            kind = document.getString(Address::kind.name),
            zipcode = document.getString(Address::zipcode.name),
            street = document.getString(Address::street.name),
            number = document.getString(Address::number.name),
            complement = document.getString(Address::complement.name),
            district = document.getString(Address::district.name),
            city = document.getString(Address::city.name),
            state = document.getString(Address::street.name),
            country = document.getString(Address::country.name),
            coordinate = document[Address::coordinate.name]?.let { CoordinateMapper.toModel(it as Document) }
        )
    }
 }