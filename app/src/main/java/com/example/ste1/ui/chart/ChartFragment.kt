package com.example.ste1.ui.chart

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.size
import com.example.ste1.MainActivity
import com.example.ste1.MainActivity.Companion.AllProduct
import com.example.ste1.R
import com.example.ste1.databinding.ChartFragmentBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.chart_fragment.*
import java.sql.Timestamp
import java.time.LocalDateTime

class ChartFragment : Fragment() {

    val TAG = "ChartFragment"
    val db = FirebaseFirestore.getInstance()

    companion object {
        fun newInstance() = ChartFragment()
    }

    private lateinit var viewModel: ChartViewModel
    private  lateinit var binding: ChartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(ChartViewModel::class.java)

        binding = ChartFragmentBinding.inflate(inflater, container,false).apply {
            lifecycleOwner = this@ChartFragment
            vmchart=viewModel

        }





        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val linechart=binding.lineChart
        linechart.description.isEnabled=false
        val xAxis =linechart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        val months= arrayOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
        val formatter =
            IndexAxisValueFormatter(months)





        var Totalen= arrayListOf<Double>(0.0 , 0.0 , 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 ,0.0 ,0.0 ,0.0 )

        var Totalfa=0.0

        val CollRef = db.collection("User").document(FirebaseAuth.getInstance()
                        .currentUser?.uid.toString()).collection("list")

        val timeM1= arrayListOf<Timestamp>()
            for ( i in 0..11 ){
                timeM1.add(Timestamp(LocalDateTime.now().year-1900,i,1
                    ,0,0,0,0))
//                Log.d(TAG,"months: "+timeM1[i].toString())
            }


        val year=LocalDateTime.now().year
        //val m1= LocalDateTime.parse("2020-01-01")
//        val m2=time.withYear(year).withMonth(2).withDayOfMonth(1)
//        val m3=time.withYear(year).withMonth(3).withDayOfMonth(1)
        // TODO: Use the ViewModel

        //Log.d(TAG+"test ",time.toString())
//        Log.d(TAG+"test ",year.toString())
       // Log.d(TAG+"test ",m1.toString())
//        Log.d(TAG+"test ",m1.toLong().toString())



        CollRef.orderBy("updateAt").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            snapshot?.forEach {


                val testDate=it?.getTimestamp("updateAt")
                val time = testDate?.toDate()
                Log.d(TAG,time?.month.toString()+" "+ it?.getString("item"))

                if(testDate!=null)
                {

                    if(testDate.toDate().toString().contains(year.toString()))
                    {
//
                        when(time?.month){
                            0->Totalen[0]=Totalen[0].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            1->Totalen[1]=Totalen[1].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            2->Totalen[2]=Totalen[2].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            3->Totalen[3]=Totalen[3].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            4->Totalen[4]=Totalen[4].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            5->Totalen[5]=Totalen[5].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            6->Totalen[6]=Totalen[6].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            7->Totalen[7]=Totalen[7].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            8->Totalen[8]=Totalen[8].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            9->Totalen[9]=Totalen[9].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            10->Totalen[10]=Totalen[10].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            11->Totalen[11]=Totalen[11].plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                            else ->Log.d(TAG,"error on month no." +time?.month.toString())
                        }


                    }

                  //  for (timeM2 in timeM1){

//                        if(testDate.toDate().time.compareTo(timeM2.time)>-1 )
//
//                    Totalen.add(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
//                    Totalfa=Totalfa.plus(AllProduct["Product1/"+it?.getString("item")]!!.fat.value!!)
//                    }


                }


                Log.d(TAG,Totalen.toString())
            }

            val enteries1 = Totalen.mapIndexed { index, d ->
                Entry(index.toFloat(), d.toFloat())
            }

            var goal=(2350)
            binding.vmchart?.goal_Ref_max?.value=(goal*1.1).toInt()
            binding.vmchart?.goal_Ref_min?.value=(goal*0.9).toInt()

           binding.seekBarS.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
               override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                   if(FirebaseAuth.getInstance().currentUser!=null&&!FirebaseAuth.getInstance().currentUser?.isAnonymous!!){
                       val info = FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                       info.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                           var age = documentSnapshot?.get("age").toString()
                           var weight = documentSnapshot?.get("weight").toString()
                           var height = documentSnapshot?.get("height").toString()
                           var sex = documentSnapshot?.get("sex").toString()

                           if (sex == "M") {
                               goal =
                                   (1.375 * (weight.toDouble() * 10 + height.toDouble() * 6.25 - age.toDouble() * 5 + 5)).roundTo(
                                       0
                                   ).toInt()
                               binding.vmchart?.goal_Ref_max?.value=(goal*1.1).toInt()
                               binding.vmchart?.goal_Ref_min?.value=(goal*0.9).toInt()
                           } else if (sex == "F") {
                               goal =
                                   (1.375 * (weight.toDouble() * 10 + height.toDouble() * 6.25 - age.toDouble() * 5 - 161)).roundTo(
                                       0
                                   ).toInt()
                               binding.vmchart?.goal_Ref_max?.value=(goal*1.1).toInt()
                               binding.vmchart?.goal_Ref_min?.value=(goal*0.9).toInt()
                           }
                       }
                   }


                   goal=(progress*30)


                   val standardList= arrayListOf<Float>(goal.toFloat(),goal.toFloat(),goal.toFloat()
                       ,goal.toFloat(),goal.toFloat(),goal.toFloat(),goal.toFloat(),goal.toFloat()
                       ,goal.toFloat(),goal.toFloat(),goal.toFloat(),goal.toFloat())

                   val enteries2=standardList.mapIndexed { index, fl ->
                       Entry(index.toFloat(),fl)
                   }

                   val lineDateSet2 = LineDataSet(enteries2,"Standard")
                    lineDateSet2.setDrawValues(false)
                   val lineDateSet = LineDataSet(enteries1,"Energy")
//                   lineDateSet.setDrawValues(true)
                   lineDateSet.setDrawFilled(true)
                   lineDateSet.fillColor=(Color.GREEN)
                   lineDateSet.setColor(Color.GREEN)
                   lineDateSet.valueTextSize=13f
                   lineDateSet2.valueTextSize=13f
//                   lineDateSet.setColors(R.color.colorAreaGreen)


                   val data = LineData(lineDateSet,lineDateSet2)
                   linechart.data =data
                   linechart.extraBottomOffset=2*6f

                   linechart.invalidate()
                  viewModel.standard.value="Goal: $progress"
               }

               override fun onStartTrackingTouch(seekBar: SeekBar?) {

               }

               override fun onStopTrackingTouch(seekBar: SeekBar?) {

               }
           })


            xAxis.granularity=1f
            xAxis.valueFormatter = formatter
            xAxis.textSize=13f
//            xAxis.mAxisMinimum=0f

            val yAxisRight = linechart.axisRight
            yAxisRight.isEnabled =false

            val yAxisLeft = linechart.axisLeft
            yAxisRight.mAxisRange=2f
            yAxisLeft.granularity =1f
//            yAxisLeft.spaceBottom=0f


            val lineDateSet = LineDataSet(enteries1,"Energy")
//            lineDateSet.setDrawValues(true)
            lineDateSet.setDrawFilled(true)
//            lineDateSet.setColors(R.color.colorAreaGreen)
//            lineDateSet.setColor(R.color.colorAreaGreen,R.color.colorPrimary)
           lineDateSet.fillColor=(Color.GREEN)
            lineDateSet.valueTextSize=13f

            lineDateSet.setColors(Color.GREEN)

            val data = LineData(lineDateSet)

//data.colors.set(R.color.colorPrimary,R.color.colorAreaGreen)

            linechart.data =data
            linechart.extraBottomOffset=3*13f


            linechart.invalidate()


//            viewModel.Total.value=Totalen.toString()


        }



    }
    fun Double.roundTo(n: Int):Double{
        return "%.${n}f".format(this).toDouble()

    }

}
