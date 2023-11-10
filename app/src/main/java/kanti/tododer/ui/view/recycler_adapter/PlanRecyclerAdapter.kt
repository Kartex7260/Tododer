package kanti.tododer.ui.view.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kanti.tododer.R
import kanti.tododer.data.model.plan.BasePlan
import kanti.tododer.data.model.plan.Plan

class PlanRecyclerAdapter(
	private val plans: List<BasePlan>,
	private val onItemClick: (BasePlan) -> Unit
) : RecyclerView.Adapter<PlanRecyclerAdapter.PlanViewHolder>() {

	inner class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val textViewTitle = view.findViewById<TextView>(R.id.textViewTodoItemPlanTitle)
		private val textViewRemark = view.findViewById<TextView>(R.id.textViewTodoItemPlanRemark)

		fun showPlan(plan: BasePlan) {
			itemView.setOnClickListener { onItemClick(plan) }

			textViewTitle.text = plan.title
			if (plan.remark.isNotBlank()) {
				textViewRemark.apply {
					text = plan.remark
					visibility = View.VISIBLE
				}
			} else {
				textViewRemark.apply {
					text = ""
					visibility = View.GONE
				}
			}
		}

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
		val view = LayoutInflater
			.from(parent.context)
			.inflate(R.layout.view_plan_list_item, parent, false)
		return PlanViewHolder(view)
	}

	override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
		val plan = plans[position]
		holder.showPlan(plan)
	}

	override fun getItemCount(): Int = plans.size

}