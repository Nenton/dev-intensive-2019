package ru.skillbranch.devintensive.ui.profile

import android.graphics.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.CircleImageView
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private var key = false
    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViewModel()
        initViews(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (outState != null) {
            super.onSaveInstanceState(outState)
            outState.putBoolean(IS_EDIT_MODE, isEditMode)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        Log.d("M_ProfileActivity", "updateUi")
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }

        val circleImageView: CircleImageView = findViewById(R.id.iv_avatar)
        if (circleImageView.drawable == null) {
            key = true
            if (Utils.toInitials(et_first_name.text.toString(), et_last_name.text.toString()) == null) {
                circleImageView.setImageResource(R.drawable.avatar_default)
            } else {
                drawInitials()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "repository" to et_repository,
            "about" to et_about,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        Log.d("M_ProfileActivity", "initViews1")

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        val textWatcher = object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!Utils.checkUrl(et_repository.text.toString())) {
                    wr_repository.error = "Невалидный адрес репозитория"
                    wr_repository.isErrorEnabled = true
                } else {
                    wr_repository.error = null
                    wr_repository.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // nothing
            }

            override fun afterTextChanged(s: Editable) {
                // nothing
            }
        }
        et_repository.addTextChangedListener(textWatcher)

        btn_edit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {

        if (!Utils.checkUrl(et_repository.text.toString())) {
            et_repository.text.replace(0, et_repository.text.length, "")
        }

        val circleImageView: CircleImageView = findViewById(R.id.iv_avatar)
        if (key) {
            if (Utils.toInitials(et_first_name.text.toString(), et_last_name.text.toString()) == null) {
                circleImageView.setImageResource(R.drawable.avatar_default)
            } else {
                Log.d("M_ProfileActivity", "saveProf")
                drawInitials()
            }
        }

        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun drawInitials() {

        val circleImageView: CircleImageView = findViewById(R.id.iv_avatar)
        val size = resources.getDimension(R.dimen.avatar_round_size_112).toInt()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        circleImageView.setImageBitmap(bitmap)

        val text = Utils.toInitials(
            Utils.transliteration(et_first_name.text.toString()),
            Utils.transliteration(et_last_name.text.toString())
        ) ?: ""
        val mPaint = Paint()
        val mTextBoundRect = Rect()

        val width = resources.getDimension(R.dimen.avatar_round_size_112)
        val height = resources.getDimension(R.dimen.avatar_round_size_112)


        val centerX = width / 2
        val centerY = height / 2

        val value = TypedValue()
        theme.resolveAttribute(R.attr.colorAccent, value, true)
        canvas.drawColor(value.data)

        Log.d("M_ProfileActivity", "smth")
        mPaint.color = Color.WHITE
        mPaint.textSize = resources.getDimension(R.dimen.avatar_initials_48)

        mPaint.getTextBounds(text, 0, text.length, mTextBoundRect)
        val mTextWidth = mPaint.measureText(text)
        val mTextHeight = mTextBoundRect.height()

        canvas.drawText(
            text,
            centerX - (mTextWidth / 2f),
            centerY + (mTextHeight / 2f),
            mPaint
        )

        circleImageView.setImageBitmap(bitmap)
    }
}