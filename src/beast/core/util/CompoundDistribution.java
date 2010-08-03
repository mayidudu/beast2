/*
* File CompoundProbabilityDistribution.java
*
* Copyright (C) 2010 Remco Bouckaert remco@cs.auckland.ac.nz
*
* This file is part of BEAST2.
* See the NOTICE file distributed with this work for additional
* information regarding copyright ownership and licensing.
*
* BEAST is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
*  BEAST is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with BEAST; if not, write to the
* Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
* Boston, MA  02110-1301  USA
*/
package beast.core.util;

import beast.core.Description;
import beast.core.Input;
import beast.core.Distribution;
import beast.core.State;
import beast.core.Input.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Description("Takes a collection of distributions, typically a number of likelihoods " +
        "and priors and combines them into the compound of these distributions " +
        "typically interpreted as the posterior.")
public class CompoundDistribution extends Distribution {
    public Input<List<Distribution>> pDistributions =
            new Input<List<Distribution>>("distribution",
                    "individual probability distributions, e.g. the likelihood and prior making up a posterior",
                    new ArrayList<Distribution>(), Validate.REQUIRED);

    /** Distribution implementation follows **/
    @Override
    public double calculateLogP() throws Exception {
        logP = 0;
        for (int i = 0; i < pDistributions.get().size(); i++) {
            double f = pDistributions.get().get(i).calculateLogP();
            logP += f;
        }
        return logP;
    }

    @Override
    public void sample(State state, Random random) {
        final List<Distribution> distributions = pDistributions.get();
        for(Distribution distribution : distributions) {
            distribution.sample(state, random);
        }
    }

    @Override
    public List<String> getArguments() {
        List<String> arguments = new ArrayList<String>();
        final List<Distribution> distributions = pDistributions.get();
        for(Distribution distribution : distributions) {
            arguments.addAll(distribution.getArguments());
        }
        return arguments;
    }

    @Override
    public List<String> getConditions() {
        List<String> conditions = new ArrayList<String>();
        final List<Distribution> distributions = pDistributions.get();
        for(Distribution distribution : distributions) {
            conditions.addAll(distribution.getConditions());
        }
        return conditions;
    }

} // class CompoundProbabilityDistribution
