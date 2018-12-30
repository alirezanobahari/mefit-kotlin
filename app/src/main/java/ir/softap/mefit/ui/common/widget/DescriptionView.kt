package ir.softap.mefit.ui.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import ir.softap.mefit.R
import ir.softap.mefit.data.model.Video
import ir.softap.mefit.utilities.extensions.colors
import ir.softap.mefit.utilities.extensions.toPx

class DescriptionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var descriptions: List<Video.VideoDetail.Description>? = null
        set(value) {
            field = value
            value?.apply {
                onEach { description ->
                    addView(buildDescriptionTitle(description.title))
                    description.subDescriptions.onEach { subDescription ->
                        addView(buildDescriptionSubtitle(subDescription.title))
                        addView(buildDescriptionText(subDescription.text))
                    }
                }
            }
        }

    var premiumDescription: Video.VideoDetail.PremiumDescription? = null
        set(value) {
            field = value
            value?.descriptions?.apply {
                if (isNotEmpty()) showPremiumDescriptionHeader()
                else showPremiumDescriptionMessage()
                onEach { description ->
                    addView(buildDescriptionTitle(description.title))
                    description.subDescriptions.onEach { subDescription ->
                        addView(buildDescriptionSubtitle(subDescription.title))
                        addView(buildDescriptionText(subDescription.text))
                    }
                }
            }
        }

    init {
        layoutDirection = View.LAYOUT_DIRECTION_RTL
        orientation = VERTICAL
    }

    private fun buildDescriptionTitle(title: String): TextView =
        TextView(context).apply {
            typeface = ResourcesCompat.getFont(context, R.font.iran_sans_mobile_bold)
            setTextColor(context.colors[R.color.colorTextLight])
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setPaddingRelative(0, 8.toPx, 0, 0)
            setSingleLine(true)
            ellipsize = TextUtils.TruncateAt.MARQUEE
            textDirection = View.TEXT_DIRECTION_RTL
            text = title
        }

    @SuppressLint("SetTextI18n")
    private fun buildDescriptionSubtitle(subTitle: String): TextView =
        TextView(context).apply {
            typeface = ResourcesCompat.getFont(context, R.font.iran_sans_mobile)
            setTextColor(context.colors[R.color.colorTextLight])
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setPaddingRelative(4.toPx, 8.toPx, 0, 0)
            setSingleLine(true)
            ellipsize = TextUtils.TruncateAt.MARQUEE
            textDirection = View.TEXT_DIRECTION_RTL
            text = "\u2022 $subTitle"
        }

    private fun buildDescriptionText(text: String): TextView =
        TextView(context).apply {
            typeface = ResourcesCompat.getFont(context, R.font.iran_sans_mobile_light)
            setTextColor(context.colors[R.color.colorTextLight])
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            setPaddingRelative(8.toPx, 8.toPx, 0, 0)
            ellipsize = TextUtils.TruncateAt.MARQUEE
            textDirection = View.TEXT_DIRECTION_RTL
            this.text = text
        }

    private fun showPremiumDescriptionHeader() {
        addView(
            View(context).apply {
                setBackgroundColor(context.colors[R.color.colorDarkGrey])
            },
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.toPx
            ).apply {
                setMargins(8.toPx, 4.toPx, 8.toPx, 4.toPx)
            }
        )
        addView(TextView(context).apply {
            typeface = ResourcesCompat.getFont(context, R.font.iran_sans_mobile_bold)
            setTextColor(context.colors[R.color.colorTextLight])
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setTextColor(context.colors[R.color.colorPrimary])
            setPadding(0, 8.toPx, 0, 0)
            setText(R.string.title_premium_description)
        })
    }

    private fun showPremiumDescriptionMessage() {
        addView(
            View(context).apply {
                setBackgroundColor(context.colors[R.color.colorDarkGrey])
            },
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.toPx
            ).apply {
                setMargins(8.toPx, 4.toPx, 8.toPx, 4.toPx)
            }
        )
        addView(LinearLayout(context).apply {
            layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            gravity = Gravity.CENTER_VERTICAL or Gravity.START
            setPaddingRelative(4.toPx, 8.toPx, 4.toPx, 8.toPx)
            isClickable = true
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            setBackgroundResource(outValue.resourceId)
        }.also {
            it.addView(
                ImageView(context).apply {
                    setImageResource(R.drawable.ic_lock)
                },
                LinearLayout.LayoutParams(24.toPx, 24.toPx).apply {
                    marginStart = 8.toPx
                    marginEnd = 8.toPx
                }
            )
            it.addView(
                TextView(context).apply {
                    typeface = ResourcesCompat.getFont(context, R.font.iran_sans_mobile)
                    setTextColor(context.colors[R.color.colorTextLight])
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    setText(R.string.msg_this_video_has_premium_description)
                },
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            )
        })
    }

}