package com.airwallet.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

private val Blue=Color(0xFF2463F5)
private val Ink=Color(0xFF18243A)
private val Muted=Color(0xFF8A9AB3)
private val Back=Color(0xFFF3F6FB)
private val Green=Color(0xFF0BA66D)
private fun money(n:Int)="₹"+NumberFormat.getNumberInstance(Locale("en","IN")).format(n)
private data class Tx(val id:String,val amount:Int,val status:String,val date:String)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{AirWalletApp()}}
}

@Composable fun AirWalletApp(){
 var logged by remember{mutableStateOf(false)}
 var page by remember{mutableStateOf("Home")}
 var uid by remember{mutableStateOf("AW100245")}
 var coins by remember{mutableIntStateOf(50668)}
 var tx by remember{mutableStateOf(listOf(Tx("AWD-1024",1000,"Completed","06 Nov 2026"),Tx("AWD-1023",500,"Completed","05 Nov 2026"),Tx("AWD-1022",250,"Pending","04 Nov 2026")))}
 MaterialTheme(colorScheme=lightColorScheme(primary=Blue,background=Back,surface=Color.White,onSurface=Ink)){
  if(!logged) AuthScreen({logged=true},{uid="AW"+(100000..999999).random();logged=true})
  else Scaffold(containerColor=Back,bottomBar={
   NavigationBar(containerColor=Color.White){
    listOf("Home","Buy","Bank","Team","My").forEach{p->
     val icon=when(p){"Home"->Icons.Default.Home;"Buy"->Icons.Default.ShoppingBag;"Bank"->Icons.Default.AccountBalanceWallet;"Team"->Icons.Default.Groups;else->Icons.Default.Person}
     NavigationBarItem(selected=page==p,onClick={page=p},icon={Icon(icon,null)},label={Text(p)},colors=NavigationBarItemDefaults.colors(selectedIconColor=Blue,selectedTextColor=Blue,indicatorColor=Blue.copy(alpha=.1f),unselectedIconColor=Muted,unselectedTextColor=Muted))
    }
   }
  }){pad->Column(Modifier.fillMaxSize().padding(pad)){Header(uid);when(page){
   "Home"->HomePage(coins,tx,{page=it})
   "Buy"->BuyPage{v->coins+=v;tx=listOf(Tx("AWD-"+(1025+tx.size),v,"Demo","Just now"))+tx;page="My"}
   "Bank"->BankPage(coins,tx)
   "Team"->TeamPage()
   else->ProfilePage(uid,coins,tx){logged=false;page="Home"}
  }}}
 }
}

@Composable fun Header(uid:String){
 Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal=18.dp,vertical=13.dp),verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(42.dp).background(Brush.linearGradient(listOf(Blue,Color(0xFF13B8D4))),CircleShape),contentAlignment=Alignment.Center){Text("AW",color=Color.White,fontWeight=FontWeight.Black)}
  Spacer(Modifier.width(11.dp));Column(Modifier.weight(1f)){Text("AirWallet",fontWeight=FontWeight.ExtraBold,fontSize=17.sp,color=Ink);Text("UID: "+uid,fontSize=11.sp,color=Muted)}
  Text("DEMO",color=Blue,fontWeight=FontWeight.Bold,fontSize=10.sp,modifier=Modifier.background(Blue.copy(alpha=.08f),RoundedCornerShape(20.dp)).padding(horizontal=11.dp,vertical=7.dp))
 }
}
@Composable fun Title(t:String,s:String=""){Column(Modifier.padding(bottom=12.dp)){Text(t,fontSize=23.sp,fontWeight=FontWeight.ExtraBold,color=Ink);if(s.isNotEmpty())Text(s,fontSize=12.sp,color=Muted)}}
@Composable fun Panel(content:@Composable ColumnScope.()->Unit){Column(Modifier.fillMaxWidth().padding(bottom=13.dp).background(Color.White,RoundedCornerShape(21.dp)).padding(17.dp),content=content)}

@Composable fun HomePage(coins:Int,tx:List<Tx>,go:(String)->Unit){
 LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp),contentPadding=PaddingValues(top=15.dp,bottom=20.dp)){
  item{
   Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF17366D),Color(0xFF111C35))),RoundedCornerShape(25.dp)).padding(21.dp)){
    Column{Text("YOUR WALLET BALANCE",color=Color(0xFFB8C8E5),fontSize=11.sp,fontWeight=FontWeight.Bold);Text(money(coins)+"  COINS",color=Color.White,fontSize=29.sp,fontWeight=FontWeight.Black,modifier=Modifier.padding(top=8.dp));Text("Equivalent demo value "+money(coins),color=Color(0xFFB8C8E5),fontSize=12.sp);Text("1 INR = 1 COIN",color=Color(0xFF6DE0B1),fontWeight=FontWeight.Bold,fontSize=12.sp,modifier=Modifier.padding(top=14.dp))}
   }
   Spacer(Modifier.height(16.dp))
   Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Buy" to Icons.Default.AddShoppingCart,"Bank" to Icons.Default.Wallet,"Team" to Icons.Default.Groups,"My" to Icons.Default.Person).forEach{(s,i)->Column(Modifier.weight(1f).background(Color.White,RoundedCornerShape(16.dp)).clickable{go(s)}.padding(vertical=14.dp),horizontalAlignment=Alignment.CenterHorizontally){Icon(i,null,tint=Blue);Text(s,fontWeight=FontWeight.Bold,fontSize=11.sp,modifier=Modifier.padding(top=6.dp))}}}
   Row(Modifier.fillMaxWidth().padding(vertical=12.dp),verticalAlignment=Alignment.CenterVertically){Title("Recent activity");Spacer(Modifier.weight(1f));Text("View all",color=Blue,fontWeight=FontWeight.Bold,modifier=Modifier.clickable{go("My")})}
  }
  items(tx.take(5)){TransactionRow(it)}
  item{Text("Demo environment: balances and orders are sample data. No real deposits, withdrawals or returns are processed.",color=Muted,fontSize=11.sp,modifier=Modifier.padding(top=8.dp).background(Color(0xFFFFF6DF),RoundedCornerShape(12.dp)).padding(12.dp))}
 }
}
@Composable fun BuyPage(onBuy:(Int)->Unit){
 val amounts=listOf(100,250,500,1000,2500,5000,10000)
 LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp),contentPadding=PaddingValues(top=18.dp,bottom=20.dp)){
  item{Title("Buy Coins","Transparent conversion · 1 INR = 1 Coin");Panel{Text("INR TO COIN CONVERSION",color=Muted,fontSize=10.sp,fontWeight=FontWeight.Bold);Text("₹1 = 1 Coin",fontSize=25.sp,fontWeight=FontWeight.Black,color=Blue,modifier=Modifier.padding(vertical=8.dp));Text("Choose a demo package. Selecting it updates only your sample wallet.",fontSize=12.sp,color=Muted)}}
  items(amounts){v->Panel{Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(money(v),fontSize=23.sp,fontWeight=FontWeight.Black);Text("You receive "+v+" coins",fontSize=12.sp,color=Blue,fontWeight=FontWeight.Bold)};Button(onClick={onBuy(v)},shape=RoundedCornerShape(13.dp)){Text("Select")}}}}
  item{Text("No payment gateway is connected in this demo.",color=Muted,fontSize=11.sp)}
 }
}
@Composable fun TransactionRow(t:Tx){
 Row(Modifier.fillMaxWidth().padding(bottom=10.dp).background(Color.White,RoundedCornerShape(16.dp)).padding(14.dp),verticalAlignment=Alignment.CenterVertically){
  Box(Modifier.size(39.dp).background(Color(0xFFEAF1FF),RoundedCornerShape(12.dp)),contentAlignment=Alignment.Center){Icon(Icons.Default.SwapHoriz,null,tint=Blue)}
  Column(Modifier.weight(1f).padding(start=10.dp)){Text(t.id,fontWeight=FontWeight.Bold,fontSize=13.sp);Text(t.date+" · "+t.status,fontSize=10.sp,color=Muted)}
  Column(horizontalAlignment=Alignment.End){Text("+"+t.amount+" coins",fontWeight=FontWeight.Bold,color=Green,fontSize=12.sp);Text(money(t.amount),fontSize=10.sp,color=Muted)}
 }
}
@Composable fun BankPage(coins:Int,tx:List<Tx>){LazyColumn(Modifier.fillMaxSize().padding(horizontal=16.dp),contentPadding=PaddingValues(top=18.dp,bottom=20.dp)){item{Title("My Wallet","Coin balance and transaction history");Panel{Text("AVAILABLE COINS",color=Muted,fontSize=11.sp);Text(coins.toString(),fontSize=34.sp,fontWeight=FontWeight.Black,color=Blue);Text("Equivalent: "+money(coins),color=Muted);Text("1 INR = 1 COIN",color=Green,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=8.dp))};Title("Wallet history")};items(tx){TransactionRow(it)};item{Text("Withdrawals are disabled in demo mode.",color=Muted,fontSize=11.sp)}}}
@Composable fun TeamPage(){Column(Modifier.fillMaxSize().padding(18.dp)){Title("My Team","Referral overview · sample figures");Panel{Text("REFERRAL CODE",color=Muted,fontSize=11.sp);Text("AWDEMO245",fontSize=25.sp,fontWeight=FontWeight.Black,color=Blue);Text("Sharing is preview-only in this demo.",color=Muted,fontSize=11.sp)};Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Level 1" to "4","Level 2" to "5","Level 3" to "3").forEach{(a,b)->Column(Modifier.weight(1f).background(Color.White,RoundedCornerShape(16.dp)).padding(12.dp)){Text(a,color=Muted,fontSize=11.sp);Text(b+" members",fontWeight=FontWeight.Bold,fontSize=14.sp)}}};Spacer(Modifier.height(14.dp));Text("Referral rewards are not accrued or paid in demo mode.",color=Muted,fontSize=12.sp)}}
@Composable fun ProfilePage(uid:String,coins:Int,tx:List<Tx>,logout:()->Unit){Column(Modifier.fillMaxSize().padding(18.dp)){Title("My Profile","Account details");Panel{Text("AirWallet Demo User",fontSize=18.sp,fontWeight=FontWeight.Bold);Text("UID: "+uid,color=Muted);HorizontalDivider(Modifier.padding(vertical=12.dp));Text("Coin balance: "+coins,fontWeight=FontWeight.Bold);Text("Transaction count: "+tx.size,color=Muted)};Panel{Text("Settings",fontWeight=FontWeight.Bold);Text("Demo account · No live payment connection",color=Muted,fontSize=12.sp,modifier=Modifier.padding(top=7.dp))};Button(onClick=logout,modifier=Modifier.fillMaxWidth(),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFFFFE9E9),contentColor=Color(0xFFD33B48)),shape=RoundedCornerShape(14.dp)){Icon(Icons.Default.Logout,null);Spacer(Modifier.width(8.dp));Text("Log out")}}}
@Composable fun AuthScreen(login:()->Unit,signup:()->Unit){
 var isSignup by remember{mutableStateOf(false)};var user by remember{mutableStateOf("")};var pass by remember{mutableStateOf("")}
 Column(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFEAF1FF),Back))).padding(24.dp),verticalArrangement=Arrangement.Center){
  Box(Modifier.size(64.dp).background(Brush.linearGradient(listOf(Blue,Color(0xFF13B8D4))),RoundedCornerShape(20.dp)),contentAlignment=Alignment.Center){Text("AW",color=Color.White,fontSize=23.sp,fontWeight=FontWeight.Black)}
  Text(if(isSignup)"Create account" else "Welcome back",fontSize=30.sp,fontWeight=FontWeight.Black,color=Ink,modifier=Modifier.padding(top=22.dp))
  Text("Sign in to your AirWallet demo",color=Muted,fontSize=13.sp,modifier=Modifier.padding(top=5.dp,bottom=20.dp))
  Panel{OutlinedTextField(user,{user=it},label={Text("Email or mobile")},modifier=Modifier.fillMaxWidth(),singleLine=true);Spacer(Modifier.height(9.dp));OutlinedTextField(pass,{pass=it},label={Text("Password")},modifier=Modifier.fillMaxWidth(),singleLine=true);Spacer(Modifier.height(16.dp));Button(onClick={if(isSignup)signup() else login()},enabled=user.isNotBlank()&&pass.isNotBlank(),modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(13.dp)){Text(if(isSignup)"Create demo account" else "Login")};TextButton(onClick={isSignup=!isSignup},modifier=Modifier.fillMaxWidth()){Text(if(isSignup)"Already registered? Login" else "New here? Create account")}}
  Text("Demo login accepts any non-empty details. No real account is created.",color=Muted,fontSize=11.sp)
 }
}
