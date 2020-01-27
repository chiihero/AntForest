package com.chii.antforest.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.chii.antforest.R;
import com.chii.antforest.task.AntFarm;
import com.chii.antforest.pojo.AlipayCooperate;
import com.chii.antforest.pojo.AlipayUser;
import com.chii.antforest.view.dialog.ChoiceDialog;
import com.chii.antforest.view.dialog.EditDialog;
import com.chii.antforest.view.dialog.ListDialog;
import com.chii.antforest.util.Config;
import com.chii.antforest.util.CooperationIdMap;
import com.chii.antforest.util.FriendIdMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends Activity {
    @BindView(R.id.cb_immediateEffect)
    Switch cbImmediateEffect;
    @BindView(R.id.cb_recordLog)
    Switch cbRecordLog;
    @BindView(R.id.cb_showToast)
    Switch cbShowToast;
    @BindView(R.id.cb_stayAwake)
    Switch cbStayAwake;
    @BindView(R.id.cb_autoRestart)
    Switch cbAutoRestart;
    @BindView(R.id.cb_collectEnergy)
    Switch cbCollectEnergy;
    @BindView(R.id.cb_helpFriendCollect)
    Switch cbHelpFriendCollect;
    @BindView(R.id.cb_receiveForestTaskAward)
    Switch cbReceiveForestTaskAward;
    @BindView(R.id.cb_cooperateWater)
    Switch cbCooperateWater;
    @BindView(R.id.btn_cooperateWaterList)
    Button btnCooperateWaterList;
    @BindView(R.id.cb_enableFarm)
    Switch cbEnableFarm;
    @BindView(R.id.cb_rewardFriend)
    Switch cbRewardFriend;
    @BindView(R.id.cb_sendBackAnimal)
    Switch cbSendBackAnimal;
    @BindView(R.id.cb_receiveFarmToolReward)
    Switch cbReceiveFarmToolReward;
    @BindView(R.id.cb_useNewEggTool)
    Switch cbUseNewEggTool;
    @BindView(R.id.cb_harvestProduce)
    Switch cbHarvestProduce;
    @BindView(R.id.cb_donation)
    Switch cbDonation;
    @BindView(R.id.cb_answerQuestion)
    Switch cbAnswerQuestion;
    @BindView(R.id.cb_receiveFarmTaskAward)
    Switch cbReceiveFarmTaskAward;
    @BindView(R.id.cb_feedAnimal)
    Switch cbFeedAnimal;
    @BindView(R.id.cb_useAccelerateTool)
    Switch cbUseAccelerateTool;
    @BindView(R.id.cb_notifyFriend)
    Switch cbNotifyFriend;
    @BindView(R.id.cb_receivePoint)
    Switch cbReceivePoint;
    @BindView(R.id.cb_openTreasureBox)
    Switch cbOpenTreasureBox;
    @BindView(R.id.cb_donateCharityCoin)
    Switch cbDonateCharityCoin;
    @BindView(R.id.cb_kbSignIn)
    Switch cbKbSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Config.shouldReload = true;
        FriendIdMap.shouldReload = true;
        CooperationIdMap.shouldReload = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cbImmediateEffect.setChecked(Config.immediateEffect());
        cbRecordLog.setChecked(Config.recordLog());
        cbShowToast.setChecked(Config.showToast());
        cbStayAwake.setChecked(Config.stayAwake());
        cbAutoRestart.setChecked(Config.autoRestart());
        cbCollectEnergy.setChecked(Config.collectEnergy());
        cbHelpFriendCollect.setChecked(Config.helpFriendCollect());
        cbReceiveForestTaskAward.setChecked(Config.receiveForestTaskAward());
        cbCooperateWater.setChecked(Config.cooperateWater());
        cbEnableFarm.setChecked(Config.enableFarm());
        cbRewardFriend.setChecked(Config.rewardFriend());
        cbSendBackAnimal.setChecked(Config.sendBackAnimal());
        cbReceiveFarmToolReward.setChecked(Config.receiveFarmToolReward());
        cbUseNewEggTool.setChecked(Config.useNewEggTool());
        cbHarvestProduce.setChecked(Config.harvestProduce());
        cbDonation.setChecked(Config.donation());
        cbAnswerQuestion.setChecked(Config.answerQuestion());
        cbReceiveFarmTaskAward.setChecked(Config.receiveFarmTaskAward());
        cbFeedAnimal.setChecked(Config.feedAnimal());
        cbUseAccelerateTool.setChecked(Config.useAccelerateTool());
        cbNotifyFriend.setChecked(Config.notifyFriend());
        cbReceivePoint.setChecked(Config.receivePoint());
        cbOpenTreasureBox.setChecked(Config.openTreasureBox());
        cbDonateCharityCoin.setChecked(Config.donateCharityCoin());
        cbKbSignIn.setChecked(Config.kbSginIn());
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (Config.hasChanged) {
            Config.hasChanged = !Config.saveConfigFile();
            Toast.makeText(this, "Configuration saved", Toast.LENGTH_SHORT).show();
        }
        FriendIdMap.saveIdMap();
        CooperationIdMap.saveIdMap();
    }

    @OnClick({R.id.cb_immediateEffect, R.id.cb_recordLog, R.id.cb_showToast, R.id.cb_stayAwake,
            R.id.cb_autoRestart, R.id.cb_collectEnergy, R.id.btn_checkInterval,
            R.id.btn_threadCount, R.id.btn_advanceTime, R.id.btn_collectInterval,
            R.id.btn_collectTimeout, R.id.btn_returnWater30, R.id.btn_returnWater20,
            R.id.btn_returnWater10, R.id.cb_helpFriendCollect, R.id.btn_dontCollectList,
            R.id.btn_dontHelpCollectList, R.id.cb_receiveForestTaskAward,
            R.id.btn_waterFriendList, R.id.cb_cooperateWater, R.id.btn_cooperateWaterList,
            R.id.cb_enableFarm, R.id.cb_rewardFriend, R.id.cb_sendBackAnimal, R.id.btn_sendType,
            R.id.btn_dontSendFriendList, R.id.btn_recallAnimalType, R.id.cb_receiveFarmToolReward,
            R.id.cb_useNewEggTool, R.id.cb_harvestProduce, R.id.cb_donation,
            R.id.cb_answerQuestion, R.id.cb_receiveFarmTaskAward, R.id.cb_feedAnimal,
            R.id.cb_useAccelerateTool, R.id.btn_feedFriendAnimalList, R.id.cb_notifyFriend,
            R.id.btn_dontNotifyFriendList, R.id.cb_receivePoint, R.id.cb_openTreasureBox,
            R.id.cb_donateCharityCoin, R.id.btn_minExchangeCount, R.id.btn_latestExchangeTime,
            R.id.cb_kbSignIn, R.id.btn_donation_developer})
    public void onViewClicked(View view) {
        Switch cb = view instanceof Switch ? (Switch) view : null;
        Button btn = view instanceof Button ? (Button) view : null;
        switch (view.getId()) {
            case R.id.cb_immediateEffect:
                Config.setImmediateEffect(cb.isChecked());
                break;
            case R.id.cb_recordLog:
                Config.setRecordLog(cb.isChecked());
                break;
            case R.id.cb_showToast:
                Config.setShowToast(cb.isChecked());
                break;
            case R.id.cb_stayAwake:
                Config.setStayAwake(cb.isChecked());
                break;
            case R.id.cb_autoRestart:
                Config.setAutoRestart(cb.isChecked());
                break;
            case R.id.cb_collectEnergy:
                Config.setCollectEnergy(cb.isChecked());
                break;
            case R.id.btn_checkInterval:
                EditDialog.showEditDialog(this, btn.getText(),Config.checkInterval() / 60_000, EditDialog.EditMode.CHECK_INTERVAL);
                break;
            case R.id.btn_threadCount:
                EditDialog.showEditDialog(this, btn.getText(),Config.threadCount(), EditDialog.EditMode.THREAD_COUNT);
                break;
            case R.id.btn_advanceTime:
                EditDialog.showEditDialog(this, btn.getText(),Config.advanceTime(), EditDialog.EditMode.ADVANCE_TIME);
                break;
            case R.id.btn_collectInterval:
                EditDialog.showEditDialog(this, btn.getText(),Config.collectInterval(),EditDialog.EditMode.COLLECT_INTERVAL);
                break;
            case R.id.btn_collectTimeout:
                EditDialog.showEditDialog(this, btn.getText(),Config.collectTimeout() / 1_000, EditDialog.EditMode.COLLECT_TIMEOUT);
                break;
            case R.id.btn_returnWater30:
                EditDialog.showEditDialog(this, btn.getText(),Config.returnWater30(), EditDialog.EditMode.RETURN_WATER_30);
                break;
            case R.id.btn_returnWater20:
                EditDialog.showEditDialog(this, btn.getText(),Config.returnWater20(), EditDialog.EditMode.RETURN_WATER_20);
                break;
            case R.id.btn_returnWater10:
                EditDialog.showEditDialog(this, btn.getText(),Config.returnWater10(), EditDialog.EditMode.RETURN_WATER_10);
                break;
            case R.id.cb_helpFriendCollect:
                Config.setHelpFriendCollect(cb.isChecked());
                break;
            case R.id.btn_dontCollectList:
                ListDialog.show(this, btn.getText(), AlipayUser.getList(),
                        Config.getDontCollectList(), null);
                break;
            case R.id.btn_dontHelpCollectList:
                ListDialog.show(this, btn.getText(), AlipayUser.getList(),
                        Config.getDontHelpCollectList(), null);
                break;
            case R.id.cb_receiveForestTaskAward:
                Config.setReceiveForestTaskAward(cb.isChecked());
                break;
            case R.id.btn_waterFriendList:
                ListDialog.show(this, btn.getText(), AlipayUser.getList(),
                        Config.getWaterFriendList(), Config.getWaterCountList());
                break;
            case R.id.cb_cooperateWater:
                Config.setCooperateWater(cb.isChecked());
                break;
            case R.id.btn_cooperateWaterList:
                ListDialog.show(this, btn.getText(), AlipayCooperate.getList(),
                        Config.getCooperateWaterList(), Config.getcooperateWaterNumList());
                break;
            case R.id.cb_enableFarm:
                Config.setEnableFarm(cb.isChecked());
                break;
            case R.id.cb_rewardFriend:
                Config.setRewardFriend(cb.isChecked());
                break;
            case R.id.cb_sendBackAnimal:
                Config.setSendBackAnimal(cb.isChecked());
                break;
            case R.id.btn_sendType:
                ChoiceDialog.showChoiceDialog(this, btn.getText(), AntFarm.SendType.names,Config.sendType().ordinal(),ChoiceDialog.ChoiceDialogMode.SEND_TYPE);
                break;
            case R.id.btn_dontSendFriendList:
                ListDialog.show(this, btn.getText(), AlipayUser.getList(),
                        Config.getDontSendFriendList(), null);
                break;
            case R.id.btn_recallAnimalType:
                ChoiceDialog.showChoiceDialog(this, btn.getText(), Config.RecallAnimalType.names,Config.recallAnimalType().ordinal(),ChoiceDialog.ChoiceDialogMode.RECALL_ANIMAL_TYPE);
                break;
            case R.id.cb_receiveFarmToolReward:
                Config.setReceiveFarmToolReward(cb.isChecked());
                break;
            case R.id.cb_useNewEggTool:
                Config.setUseNewEggTool(cb.isChecked());
                break;
            case R.id.cb_harvestProduce:
                Config.setHarvestProduce(cb.isChecked());
                break;
            case R.id.cb_donation:
                Config.setDonation(cb.isChecked());
                break;
            case R.id.cb_answerQuestion:
                Config.setAnswerQuestion(cb.isChecked());
                break;
            case R.id.cb_receiveFarmTaskAward:
                Config.setReceiveFarmTaskAward(cb.isChecked());
                break;
            case R.id.cb_feedAnimal:
                Config.setFeedAnimal(cb.isChecked());
                break;
            case R.id.cb_useAccelerateTool:
                Config.setUseAccelerateTool(cb.isChecked());
                break;
            case R.id.btn_feedFriendAnimalList:
                ListDialog.show(this, btn.getText(), AlipayUser.getList(),
                        Config.getFeedFriendAnimalList(), Config.getFeedFriendCountList());
                break;
            case R.id.cb_notifyFriend:
                Config.setNotifyFriend(cb.isChecked());
                break;
            case R.id.btn_dontNotifyFriendList:
                ListDialog.show(this, btn.getText(), AlipayUser.getList(),
                        Config.getDontNotifyFriendList(), null);
                break;
            case R.id.cb_receivePoint:
                Config.setReceivePoint(cb.isChecked());
                break;
            case R.id.cb_openTreasureBox:
                Config.setOpenTreasureBox(cb.isChecked());
                break;
            case R.id.cb_donateCharityCoin:
                Config.setDonateCharityCoin(cb.isChecked());
                break;
            case R.id.btn_minExchangeCount:
                EditDialog.showEditDialog(this, btn.getText(),Config.minExchangeCount(),
                        EditDialog.EditMode.MIN_EXCHANGE_COUNT);
                break;
            case R.id.btn_latestExchangeTime:
                EditDialog.showEditDialog(this, btn.getText(),Config.latestExchangeTime(),
                        EditDialog.EditMode.LATEST_EXCHANGE_TIME);
                break;
            case R.id.cb_kbSignIn:
                Config.setKbSginIn(cb.isChecked());
                break;
//            case R.id.btn_donation_developer:
//                Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi" +
//                        "/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay" +
//                        ".com%2Ftsx00339eflkuhhtfctcn48"));
//                startActivity(it2);
//                break;
            default:
        }
    }
}

