package kr.co.lion.android_homeworkproject_two

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.android_homeworkproject_two.databinding.ActivityInputBinding
import kr.co.lion.android_homeworkproject_two.databinding.ActivityMainBinding
import kr.co.lion.android_homeworkproject_two.databinding.ActivityShowInfoBinding
import kr.co.lion.android_homeworkproject_two.databinding.RowMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // InputActivity의 런처
    lateinit var inputActivityLauncher:ActivityResultLauncher<Intent>

    // ShowInfoActivity의 런처
    lateinit var showInfoActivityLauncher:ActivityResultLauncher<Intent>

    // ModifyActivity의 런처
    lateinit var modifyActivityLauncher:ActivityResultLauncher<Intent>

    // 메모의  정보를 담을 리스트
    val memoDataList = mutableListOf<MemoData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initData()
        setToolbar()
        setView()

    }

    // 기본 데이터 및 객체 셋팅
    fun initData(){
        val contract1 = ActivityResultContracts.StartActivityForResult()
        inputActivityLauncher = registerForActivityResult(contract1){
            // 작업 결과가 OK 라면.
            if(it.resultCode == RESULT_OK){
                // 전달된 Intent객체가 있다면
                if(it.data != null){
                    // 메모 객체를 추출해서 리스트에 담고 갱신.
                    if(Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU){
                        val memoData = it.data?.getParcelableExtra("memoData", MemoData::class.java)
                        memoDataList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    } else{
                        val memoData = it.data?.getParcelableExtra<MemoData>("memoData")
                        memoDataList.add(memoData!!)
                        activityMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        // ShowInfoActivity 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        showInfoActivityLauncher = registerForActivityResult(contract2){

        }

        // ModifyActivity 런처
        val contract3 = ActivityResultContracts.StartActivityForResult()
        modifyActivityLauncher = registerForActivityResult(contract3){

        }
    }

    // 툴바 구성
    fun setToolbar(){
        activityMainBinding.apply {
            toolbarMain.apply {
                title = "메모 관리"
                // 메뉴 등록
                inflateMenu(R.menu.menu_main)
                // 메뉴의 리스너
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_main_add -> {
                            val inputIntent = Intent(this@MainActivity, InputActivity::class.java)
                            inputActivityLauncher.launch(inputIntent)
                        }
                    }
                    true
                }
            }
        }
    }

    // View 구성
    fun setView(){
        activityMainBinding.apply {
            recyclerViewMain.apply {
                // 어뎁터 설정
                adapter = RecyclerViewMainAdapter()
                // 레이아웃 매니저
                layoutManager = LinearLayoutManager(this@MainActivity)
                // 구분선
                val deco = MaterialDividerItemDecoration(this@MainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    inner class RecyclerViewMainAdapter: RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>(){

        inner class ViewHolderMain(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root){
            val rowMainBinding: RowMainBinding

            init {
                this.rowMainBinding = rowMainBinding

                // 항목 클릭 시 전체가 클릭 되도록.
                this.rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                // 항목을 눌렀을 때의 리스너
                this.rowMainBinding.root.setOnClickListener {
                    // ShowInfoActivirt를 실행한다
                    val showInfoIntent = Intent(this@MainActivity, ShowInfoActivity::class.java)

                    // 선택한 항목 번째 객체를 Intent에 담아준다.
                    showInfoIntent.putExtra("memoData", memoDataList[adapterPosition])

                    showInfoActivityLauncher.launch(showInfoIntent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return memoDataList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMainTitle.text = "${memoDataList[position].memoTitle}"
            holder.rowMainBinding.textViewRowMainDate.text = "${memoDataList[position].memoDate}"
        }
    }

}