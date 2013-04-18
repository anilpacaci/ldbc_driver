/**                                                                                                                                                                                
 * Copyright (c) 2010 Yahoo! Inc. All rights reserved.                                                                                                                             
 *                                                                                                                                                                                 
 * Licensed under the Apache License, Version 2.0 (the "License"); you                                                                                                             
 * may not use this file except in compliance with the License. You                                                                                                                
 * may obtain a copy of the License at                                                                                                                                             
 *                                                                                                                                                                                 
 * http://www.apache.org/licenses/LICENSE-2.0                                                                                                                                      
 *                                                                                                                                                                                 
 * Unless required by applicable law or agreed to in writing, software                                                                                                             
 * distributed under the License is distributed on an "AS IS" BASIS,                                                                                                               
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or                                                                                                                 
 * implied. See the License for the specific language governing                                                                                                                    
 * permissions and limitations under the License. See accompanying                                                                                                                 
 * LICENSE file.                                                                                                                                                                   
 */

package com.yahoo.ycsb;

import java.util.Map;
import java.util.Properties;

public class Utils
{
    // /**
    // * Generate a random ASCII string of a given length.
    // */
    // public static String ASCIIString( int length )
    // {
    // int interval = '~' - ' ' + 1;
    //
    // byte[] buf = new byte[length];
    // random().nextBytes( buf );
    // for ( int i = 0; i < length; i++ )
    // {
    // if ( buf[i] < 0 )
    // {
    // buf[i] = (byte) ( ( -buf[i] % interval ) + ' ' );
    // }
    // else
    // {
    // buf[i] = (byte) ( ( buf[i] % interval ) + ' ' );
    // }
    // }
    // return new String( buf );
    // }

    /**
     * Hash an integer value.
     */
    public static long hash( long val )
    {
        return FNVhash64( val );
    }

    public static final int FNV_offset_basis_32 = 0x811c9dc5;
    public static final int FNV_prime_32 = 16777619;

    /**
     * 32 bit FNV hash. Produces more "random" hashes than (say)
     * String.hashCode().
     * 
     * @param val The value to hash.
     * @return The hash value
     */
    public static int FNVhash32( int val )
    {
        // from http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash
        int hashval = FNV_offset_basis_32;

        for ( int i = 0; i < 4; i++ )
        {
            int octet = val & 0x00ff;
            val = val >> 8;

            hashval = hashval ^ octet;
            hashval = hashval * FNV_prime_32;
            // hashval = hashval ^ octet;
        }
        return Math.abs( hashval );
    }

    public static final long FNV_offset_basis_64 = 0xCBF29CE484222325L;
    public static final long FNV_prime_64 = 1099511628211L;

    /**
     * 64 bit FNV hash. Produces more "random" hashes than (say)
     * String.hashCode().
     * 
     * @param val The value to hash.
     * @return The hash value
     */
    public static long FNVhash64( long val )
    {
        // from http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash
        long hashval = FNV_offset_basis_64;

        for ( int i = 0; i < 8; i++ )
        {
            long octet = val & 0x00ff;
            val = val >> 8;

            hashval = hashval ^ octet;
            hashval = hashval * FNV_prime_64;
            // hashval = hashval ^ octet;
        }
        return Math.abs( hashval );
    }

    public static <K, V> V mapGetDefault( Map<K, V> map, K key, V defaultValue )
    {
        return ( map.containsKey( key ) ) ? map.get( key ) : defaultValue;
    }

    // public static Map<String, String> propertiesToMap( Properties
    // fromProperties, Map<String, String> toMap,
    // boolean overwrite )
    // {
    // for ( Object fromPropertyKey : fromProperties.keySet() )
    // {
    // if ( ( overwrite ) || ( false == toMap.containsKey( (String)
    // fromPropertyKey ) ) )
    // {
    // toMap.put( (String) fromPropertyKey, (String) fromProperties.get(
    // fromPropertyKey ) );
    // }
    // }
    // return toMap;
    // }

    public static <K, V> Map<K, V> mergePropertiesToMap( Properties fromProperties, Map<K, V> toMap, boolean overwrite )
    {
        for ( Object fromPropertyKey : fromProperties.keySet() )
        {
            if ( ( overwrite ) || ( false == toMap.containsKey( (K) fromPropertyKey ) ) )
            {
                toMap.put( (K) fromPropertyKey, (V) fromProperties.get( (K) fromPropertyKey ) );
            }
        }
        return toMap;
    }

    public static <K, V> Properties mergeMapToProperties( Map<K, V> fromMap, Properties toProperties, boolean overwrite )
    {
        for ( K fromMapKey : fromMap.keySet() )
        {
            if ( ( overwrite ) || ( false == toProperties.containsKey( fromMapKey ) ) )
            {
                toProperties.put( fromMapKey, fromMap.get( fromMapKey ) );
            }
        }
        return toProperties;
    }

    public static <K, V> Map<K, V> mergeMaps( Map<K, V> fromMap, Map<K, V> toMap, boolean overwrite )
    {
        for ( K fromMapKey : fromMap.keySet() )
        {
            if ( ( overwrite ) || ( false == toMap.containsKey( fromMapKey ) ) )
            {
                toMap.put( fromMapKey, fromMap.get( fromMapKey ) );
            }
        }
        return toMap;
    }

}