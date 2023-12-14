package kanti.tododer.data.room.plan

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType

@Entity(tableName = "plans")
data class PlanEntity(
	@PrimaryKey(autoGenerate = true) override val id: Int = 0,
	override val title: String = "",
	override val archived: Boolean = false,
	@ColumnInfo(name = "type") val typeString: String = PlanType.DefaultValue.toString()
) : Plan {

	@get:Ignore
	override val type: PlanType
		get() = PlanType.valueOf(typeString)
}

fun Plan.toPlanEntity(
	id: Int = this.id,
	title: String = this.title,
	archived: Boolean = this.archived,
	type: PlanType = this.type
): PlanEntity {
	if (
		this is PlanEntity &&
		id == this.id &&
		title == this.title &&
		archived == this.archived &&
		type == this.type
	)
		return this
	return PlanEntity(
		id = id,
		title = title,
		archived = archived,
		typeString = type.toString()
	)
}
