/*
 * Copyright (C) 2004 Felipe Gustavo de Almeida
 * Copyright (C) 2010-2015 The MPDroid Project
 *
 * All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice,this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.anpmech.mpd.connection;

import com.anpmech.mpd.commandresponse.CommandResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the core of the {@link CommandResponse} classes, abstracted for the Android backend.
 *
 * <p>This class contains the bare results from the connection. Processing required from this
 * result should be done in a subclass.</p>
 *
 * <p>This class is subclassed to process any MPD protocol server responses. This class is
 * immutable, thus, thread-safe.</p>
 */
public class CommandResult extends AbstractCommandResult implements Parcelable {

    /**
     * This is the ClassLoader to use when unparceling this class.
     */
    public static final ClassLoader LOADER = CommandResult.class.getClassLoader();

    /**
     * This field is used to instantiate this class from a {@link Parcel}.
     */
    protected static final Creator<CommandResult> CREATOR = new CommandResultCreator();

    /**
     * This is an empty no-op CommandResult.
     */
    protected static final CommandResult EMPTY = new CommandResult();

    /**
     * This constructor is used to subclass a CommandResult.
     *
     * @param result The result to subclass.
     */
    protected CommandResult(final CommandResult result) {
        this(result.mConnectionResult, result.mResult, result.mExcludeResponses, result.mListSize);
    }

    /**
     * This constructor is used to create a new core result from the MPD protocol.
     *
     * @param connectionResult The result of the connection initiation.
     * @param result           The MPD protocol command result.
     * @param excludeResponses This is used to manually exclude responses from a split iterator.
     */
    protected CommandResult(final String connectionResult, final String result,
            final int[] excludeResponses) {
        this(connectionResult, result, excludeResponses, 16);
    }

    /**
     * This constructor is used to create a new core result from the MPD protocol.
     *
     * @param connectionResult The result of the connection initiation.
     * @param result           The MPD protocol command result.
     * @param excludeResponses This is used to manually exclude responses from a split iterator.
     * @param listSize         This is the size of this object if it is created as a {@link
     *                         java.util.List}; which is to say how many newlines + 1 which can be
     *                         found in the {@link #mResult} field. This value is simply a helper,
     *                         and, is typically, generated during first iteration.
     */
    private CommandResult(final String connectionResult, final String result,
            final int[] excludeResponses, final int listSize) {
        super(connectionResult, result, excludeResponses, listSize);
    }

    /**
     * This constructor is used to create a new empty CommandResult.
     *
     * @see #EMPTY
     */
    private CommandResult() {
        super();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mConnectionResult);
        dest.writeString(mResult);
        dest.writeIntArray(mExcludeResponses);
        dest.writeInt(mListSize);
    }

    /**
     * This class is used to instantiate a CommandResult from a {@code Parcel}.
     */
    private static final class CommandResultCreator implements Creator<CommandResult> {

        /**
         * Sole constructor.
         */
        private CommandResultCreator() {
            super();
        }

        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public CommandResult createFromParcel(final Parcel source) {
            return new CommandResult(source.readString(), source.readString(),
                    source.createIntArray(), source.readInt());
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public CommandResult[] newArray(final int size) {
            return new CommandResult[size];
        }
    }
}