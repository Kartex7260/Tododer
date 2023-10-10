package kanti.tododer.ui.view.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kanti.tododer.R
import kanti.tododer.data.model.plan.Plan

class PlanRecyclerAdapter(
	private val plans: List<Plan>,
	private val onItemClick: (Plan) -> Unit
) : RecyclerView.Adapter<PlanRecyclerAdapter.PlanViewHolder>() {

	inner class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val textViewTitle = view.findViewById<TextView>(R.id.textViewPlanRemark)
		private val textViewRemark = view.findViewById<TextView>(R.id.textViewPlanRemark)

		fun showPlan(plan: Plan) {
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