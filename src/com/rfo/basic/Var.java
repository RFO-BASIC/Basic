/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2015 Paul Laughton

This file is part of BASIC! for Android

    BASIC! is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BASIC! is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BASIC!.  If not, see <http://www.gnu.org/licenses/>.

    You may contact the author or current maintainers at http://rfobasic.freeforums.org

    Apache Commons Net
    Copyright 2001-2011 The Apache Software Foundation

    This product includes software developed by
    The Apache Software Foundation (http://www.apache.org/).

*************************************************************************************************/

package com.rfo.basic;

//Log.v(LOGTAG, "Line Buffer  " + ExecutingLineBuffer);

import java.security.InvalidParameterException;
import java.util.ArrayList;

import android.util.Log;

public abstract class Var {

	// ****************************** Type enum *******************************

	public enum Type {									// a variable can be a string or a number
		NOVAR("X", "none", false, false),
		NUM("N", "numeric", true, false),
		STR("S", "string", false, true);

		private final String mCh;
		private final String mStr;
		private final boolean mIsNumeric;
		private final boolean mIsString;
		private Type(String shortForm, String longForm, boolean isNumeric, boolean isString) {
			mCh = shortForm; mStr = longForm;
			mIsNumeric = isNumeric; mIsString = isString;
		}

		public static Type typeOf(char c) {
			switch (c) {
				case 'n': return NUM;
				case 's': return STR;
				default:  return NOVAR;
			}
		}

		// Use this to get standardized error message.
		public Type isNS() {							// allows only NUM or STR
			if (this == NOVAR) { throw new InvalidParameterException("Untyped variable."); }
			return this;
		}

		public String typeNS() {						// allows only NUM or STR
			isNS();
			return mCh;
		}

		public boolean isNumeric() { return mIsNumeric; }
		public boolean isString() { return mIsString; }
		@Override
		public String toString() { return mStr; }
	}

	// ****************************** Val class *******************************
	// An object of this class holds the value of a scalar variable or an array element.

	protected abstract static class Val {
		protected static final String WRONG_TYPE = "Wrong type for this variable.";

		protected Val() { }

		protected abstract boolean isNumeric();
		protected abstract boolean isString();
		protected abstract void val(double val);
		protected abstract void val(String val);
		protected abstract void addval(double val);
		protected abstract void addval(String val);
		protected abstract double nval();
		protected abstract String sval();
	} // class Val

	public static class NumVal extends Val {
		private double mVal;

		public NumVal() { this(0.0); }
		public NumVal(double val) { mVal = val; }

		@Override public boolean isNumeric() { return true; }
		@Override public boolean isString() { return false; }

		@Override public void val(double val) { mVal = val; }
		@Override public void val(String val) { throw new InvalidParameterException(WRONG_TYPE); }

		@Override public void addval(double val) { mVal += val; }
		@Override public void addval(String val) { throw new InvalidParameterException(WRONG_TYPE); }

		@Override public double nval() { return mVal; }
		@Override public String sval() { throw new InvalidParameterException(WRONG_TYPE); }
	} // class NumVal

	public static class StrVal extends Val {
		private String mVal;

		public StrVal() { this(""); }
		public StrVal(String val) { mVal = val; }

		@Override public boolean isNumeric() { return false; }
		@Override public boolean isString() { return true; }

		@Override public void val(double val) { throw new InvalidParameterException(WRONG_TYPE); }
		@Override public void val(String val) { mVal = val; }

		@Override public void addval(double val) { throw new InvalidParameterException(WRONG_TYPE); }
		@Override public void addval(String val) { mVal += val; }

		@Override public double nval() { throw new InvalidParameterException(WRONG_TYPE); }
		@Override public String sval() { return mVal; }
	} // class StrVal

	public static class ArrayVal extends Val {
		private ArrayDef mArray;						// where the array data is
		private Val mVal;								// a NumVal or StrVal, holds the value of an element
		private int mIndex;								// the index of the element in the array in mArray

		// Initialize with a new scalar Val.
		public ArrayVal(ArrayDef array, boolean isNumeric) {
			mArray = array;
			mVal = (isNumeric) ? new NumVal() : new StrVal();
			mIndex = -1;
		}
		// Initialize with an existing scalar Val.
		public ArrayVal(ArrayDef array, Val val) {
			mArray = array;
			mVal = val;
			mIndex = -1;
		}

		@Override public boolean isNumeric() { return (mVal != null) && mVal.isNumeric(); }
		@Override public boolean isString() { return (mVal != null) && mVal.isString(); }

		// Save a (1D) index and cache the current value of the array at that index.
		public void index(int index, double val) {		// numeric array
			mIndex = index;
			mVal.val(val);
		}
		public void index(int index, String val) {		// string array
			mIndex = index;
			mVal.val(val);
		}

		@Override public void val(double val) {
			mVal.val(val);								// write the local Val
			mArray.val(val, mIndex);					// write the array, too
		}
		@Override public void val(String val) {
			mVal.val(val);								// write the local Val
			mArray.val(val, mIndex);					// write the array, too
		}

		@Override public void addval(double val) {
			mVal.addval(val);							// write the local Val
			mArray.val(mVal.nval(), mIndex);			// write the array, too
		}
		@Override public void addval(String val) {
			mVal.addval(val);							// write the local Val
			mArray.val(mVal.sval(), mIndex);			// write the array, too
		}

		@Override public double nval() { return mVal.nval(); }	// read the cached value
		@Override public String sval() { return mVal.sval(); }	// read the cached value
	} // class StrVal

	// **************************** ArrayDef class ****************************
	// Array definition: metadata and data

	public static class ArrayDef {
		private ArrayList<Integer> mDimList;
		private ArrayList<Integer> mArraySizes;
		private int mLength;
		private boolean mValid;
		private double[] mNum;
		private String[] mStr;

		public ArrayDef(ArrayList<Integer> dimList) {
			// Record the dimensions of a basic array and calculate the total length.
			// Caller must call setArray(int, boolean) to attach a real array.

			// This list of sizes is used to quickly calculate the array element offset
			// when the array is referenced.
			ArrayList<Integer> arraySizes = new ArrayList<Integer>();
			int length = 1;
			for (int d = dimList.size() - 1; d >= 0; --d) {	// for each Dim from last to first
				int dim = dimList.get(d);				// get the Dim
				if (dim < 1) { throw new InvalidParameterException("ArrayDef dimList"); }
				arraySizes.add(0, length);				// insert the previous total in the ArraySizes List
				length *= dim;							// multiply this dimension by the previous size
			}
			mDimList = dimList;
			mArraySizes = arraySizes;
			mLength = length;
			invalidate();								// not yet valid
		}

		public void invalidate() {						// mark invalid and release all data
			mValid = false;
			mNum = null;
			mStr = null;
		}

		public boolean valid() { return mValid; }
		public int length() { return mLength; }
		public ArrayList<Integer> dimList() { return mDimList; }
		public ArrayList<Integer> arraySizes() { return mArraySizes; }

		public void createArray(boolean isNumeric) {	// create and initialize empty array
			if (isNumeric) {
				mStr = null;
				mNum = new double[mLength];				// initialized to 0.0
			} else {
				mStr = new String[mLength];				// initialized to null
				mNum = null;
				for (int i = 0; i < mLength; ++i) { mStr[i] = ""; }
			}
			mValid = true;
		}

		private int getIndex(ArrayList<Integer> indexList) {
			int nDims = mDimList.size();
			int nIndices = indexList.size();
			if (nDims != nIndices) {					// insure index count = dim count
				throw new InvalidParameterException(
						"Expected " + nDims +
						" ind" + ((nDims == 1) ? "ex" : "ices") +
						" but found " + nIndices + ":");
			}

			int index = 0;
			for (int i = 0; i < nDims; ++i) {
				int p = indexList.get(i);				// p = index for this call
				int q = mDimList.get(i);				// q = DIMed value for this index
				int r = mArraySizes.get(i);				// r = size for this index
				if (p > q) {							// insure index <= DIMed limit
					throw new InvalidParameterException(
							"Index #"+ (i+1) + " (" + p +
							") exceeds dimension (" + q +
							") at:");
				}
				if (p <= 0) {							// insure index >= 1
					throw new InvalidParameterException("Index must be >=1 at:");
				}
				index += (p-1)*r;						// calculate index
			}
			return index;
		}

		// Index the array, put the index and current element value in the ArrayVal.
		public void val(ArrayVal val, ArrayList<Integer> indexList) {
			int index = getIndex(indexList);
			if (val.isNumeric()) { val.index(index, mNum[index]); }
			else                 { val.index(index, mStr[index]); }
		}

		// Raw element access: assume 1D, skip index calculation.
		// Also skip index checking. Java will do it, but caller should
		// check so user doesn't have to deal with the error.
		public void val(double val, int index) {
			mNum[index] = val;
		}
		public void val(String val, int index) {
			mStr[index] = val;
		}
		public double nval(int index) {
			return mNum[index];
		}
		public String sval(int index) {
			return mStr[index];
		}

		// Raw array access.
		public double[] getNumArray() { return mNum; }
		public String[] getStrArray() { return mStr; }
	} // class ArrayDef

	// *********************** FunctionParameter class ************************

	public static class FunctionParameter {
		private final Var mVar;							// parameter name, type, and value
		private boolean mIsGlobal = false;

		public FunctionParameter(Var var) { mVar = var; }

		public void isGlobal(boolean isGlobal) { mIsGlobal = isGlobal; }

		public Var var() { return mVar; }
		public boolean isGlobal() { return mIsGlobal; }
	}

	// ***************************** FnDef class ******************************
	// Function Definition: attributes defined by FN.DEF.

	public static class FnDef {
		private int mLine;								// line number of fn.def
		private Var mVar;								// name and type of this function
		private ArrayList<FunctionParameter> mParms;	// list of function parameters

		public FnDef(int line, Var var, ArrayList<FunctionParameter> parms) {
			mLine = line; mVar = var; mParms = parms;
		}

		public int line() { return mLine; }
		public String name() { return mVar.name(); }
		public Type type() { return mVar.type(); }
		public int nParms() { return mParms.size(); }
		public ArrayList<FunctionParameter> parms() { return mParms; }
	}

	// ******************************* Var class *******************************
	// An object of this class holds the name and type of a variable.
	// Abstract class. Use one of the three subclasses below.

	protected String mName;
	protected Type mType;
	protected boolean mNew = true;

	// Constructors
	protected Var(String name, boolean isNumeric) { copyCommon(name, isNumeric); }
	protected Var(String name, Type type)         { copyCommon(name, type); }

	// Copy fields of specified Var to this one. These make shallow copies.
	protected final void copyCommon(Var from) {
		mName = from.name();
		mType = from.type();
	}
	protected final void copyCommon(String name, Type type) {
		mName = name;
		mType = type;
	}
	protected final void copyCommon(String name, boolean isNumeric) {
		mName = name;
		mType = isNumeric ? Type.NUM : Type.STR;
	}

	// Setters
	public void notNew() { mNew = false; }				// mark "not new"
	public void reNew() { mNew = true; }				// mark "new", keep name and type unchanged
	public Var reNew(String name, boolean isNumeric) {	// mark "new", use caller's name and type
		mNew = true;
		copyCommon(name, isNumeric);
		return this;
	}

	protected abstract Val newVal();					// attach a new Val to this Var and return it
	protected abstract void val(Val val);				// attach an existing Val to this Var
	protected void arrayDef(ArrayDef def) {				// attach an ArrayDef to this Var (ArrayVar only)
		throw new InvalidParameterException("Var is not ArrayVar");
	}
	protected void fnDef(FnDef def) {					// attach a FnDef to this Var (FunctionVar only)
		throw new InvalidParameterException("Var is not FunctionVar");
	}

	// Create a new Var just like this one.
	protected abstract Var clone();

	// Getters
	protected String name() { return mName; }
	protected Type type() { return (mType != null) ? mType : Type.NOVAR; }
	protected boolean isNumeric() { return type().isNumeric(); }
	protected boolean isString() { return type().isString(); }
	protected boolean isArray() { return false; }
	protected boolean isFunction() { return false; }
	public boolean isNew() { return mNew; }

	protected abstract Val val();						// return the Val attached to this Var
	protected ArrayDef arrayDef() {
		throw new InvalidParameterException("Var is not ArrayVar");
	}
	protected FnDef fnDef() {
		throw new InvalidParameterException("Var is not FunctionVar");
	}

	// *************************** ScalarVar subclass **************************

	public static class ScalarVar extends Var {
		private Val mVal = null;

		public ScalarVar(String name, boolean isNumeric) { super(name, isNumeric); }
		public ScalarVar(String name, Type type)         { super(name,  type); }

		@Override public Val newVal() {						// attach a new scalar Val to this ScalarVar and return it
			mVal = isNumeric() ? new NumVal() : new StrVal();
			return mVal;
		}
		@Override public void val(Val val) { mVal = val; }	// attach an existing Val to this Var
		@Override public Var clone() {						// make a new Var, shallow copy of this Var
			Var var = new ScalarVar(mName, mType.isNumeric());
			var.val(mVal);
			return var;
		}

		@Override public Val val() {						// return the Val attached to this ScalarVar
			if (mVal == null) { throw new InvalidParameterException("ScalarVar has no Val"); }
			return mVal;
		}
	} // class ScalarVar

	// *************************** ArrayVar subclass ***************************

	public static class ArrayVar extends Var {
		private ArrayDef mArrayDef = null;					// array metadata and data
		private ArrayVal mVal;								// caches the index and data of an array element

		public ArrayVar(String name, boolean isNumeric) {
			super(name, isNumeric);
		}
		public ArrayVar(String name, Type type) {
			super(name,  type);
		}

		@Override public Var reNew(String name, boolean isNumeric) {
			super.reNew(name, isNumeric);					// mark "new", use caller's name and type
			mArrayDef = null;								// destroy array, no need to destroy mVal
			return this;
		}

		@Override public Val newVal() {
			throw new InvalidParameterException("ArrayVar.val(Val): must specify index(es)");
		}
		@Override public void val(Val val) { newVal(); }	// throw exception
		@Override public void arrayDef(ArrayDef def) {		// attach an existing ArrayDef and create an ArrayVal
			mArrayDef = def;
			mVal = new Var.ArrayVal(def, isNumeric());		// create a new ArrayVal of the right type
;		}

		@Override public Var clone() {						// make a new Var, shallow copy of this Var
			ArrayVar var = new ArrayVar(mName, isNumeric());
			var.arrayDef(mArrayDef);						// reference same ArrayDef, make a new Val
			return var;
		}

		@Override public boolean isArray() { return true; }

		@Override public Val val() {
			throw new InvalidParameterException("ArrayVar.val(): must specify index(es)");
		}
		// Replaces "Val val()": index an array, put the index and current element value in mVal.
		public Val val(ArrayList<Integer> indexList) {
			mArrayDef.val(mVal, indexList);
			return mVal;
		}

		@Override public ArrayDef arrayDef() {				// return the ArrayDef attached to this ArrayVar
			if (mArrayDef == null) { throw new InvalidParameterException("ArrayVar has no ArrayDef"); }
			return mArrayDef;
		}
	} // class ArrayVar

	// ************************** FunctionVar subclass *************************

	public static class FunctionVar extends Var {
		private FnDef mFnDef = null;

		public FunctionVar(String name, boolean isNumeric) { super(name, isNumeric); }
		public FunctionVar(String name, Type type)         { super(name,  type); }

		@Override public Val newVal() {
			throw new InvalidParameterException("FunctionVar has no Val");
		}
		@Override public void val(Val val) { newVal(); }		// throw exception
		@Override public void fnDef(FnDef def) { mFnDef = def; }	// attach an existing FnDef

		@Override public Var clone() {							// make a new Var, shallow copy of this Var
			Var var = new FunctionVar(mName, mType.isNumeric());
			var.fnDef(mFnDef);
			return var;
		}

		@Override public boolean isFunction() { return true; }

		@Override public Val val() { return newVal(); }			// throw exception

		@Override public FnDef fnDef() {						// return the FnDef attached to this FunctionVar
			if (mFnDef == null) { throw new InvalidParameterException("FunctionVar has no FnDef"); }
			return mFnDef;
		}
	} // class FunctionVar
}