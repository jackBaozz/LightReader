/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Users\\Asus\\eclipse_workspace\\LightReader\\src\\com\\lightreader\\bzz\\temp\\IDog.aidl
 */
package com.lightreader.bzz.temp;
public interface IDog extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lightreader.bzz.temp.IDog
{
private static final java.lang.String DESCRIPTOR = "com.lightreader.bzz.temp.IDog";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lightreader.bzz.temp.IDog interface,
 * generating a proxy if needed.
 */
public static com.lightreader.bzz.temp.IDog asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lightreader.bzz.temp.IDog))) {
return ((com.lightreader.bzz.temp.IDog)iin);
}
return new com.lightreader.bzz.temp.IDog.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getAge:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAge();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_gender:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.gender();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getMes:
{
data.enforceInterface(DESCRIPTOR);
User _arg0;
if ((0!=data.readInt())) {
_arg0 = User.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<Message> _result = this.getMes(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lightreader.bzz.temp.IDog
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getAge() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAge, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int gender() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_gender, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 在AIDL接口中定义一个getMes方法

@Override public java.util.List<Message> getMes(User us) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<Message> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((us!=null)) {
_data.writeInt(1);
us.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_getMes, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(Message.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getAge = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_gender = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getMes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public java.lang.String getName() throws android.os.RemoteException;
public int getAge() throws android.os.RemoteException;
public int gender() throws android.os.RemoteException;
// 在AIDL接口中定义一个getMes方法

public java.util.List<Message> getMes(User us) throws android.os.RemoteException;
}
