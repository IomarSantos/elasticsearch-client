/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spatial4j.core.shape.jts;


import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Rectangle;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.SpatialRelation;
import com.spatial4j.core.shape.impl.PointImpl;
import com.vividsolutions.jts.geom.CoordinateSequence;

/** Wraps a {@link com.vividsolutions.jts.geom.Point}. */
public class JtsPoint implements Point {

  private final SpatialContext ctx;
  private com.vividsolutions.jts.geom.Point pointGeom;

  /** A simple constructor without normalization / validation. */
  public JtsPoint(com.vividsolutions.jts.geom.Point pointGeom, SpatialContext ctx) {
    this.ctx = ctx;
    this.pointGeom = pointGeom;
  }

  public com.vividsolutions.jts.geom.Point getGeom() {
    return pointGeom;
  }

  public com.spatial4j.core.shape.Point getCenter() {
    return this;
  }

  public boolean hasArea() {
    return false;
  }

  public double getArea(SpatialContext ctx) {
    return 0;
  }

  public Rectangle getBoundingBox() {
    return ctx.makeRectangle(this, this);
  }

  public SpatialRelation relate(Shape other) {
    // ** NOTE ** the overall order of logic is kept consistent here with simple.PointImpl.
    if (other instanceof com.spatial4j.core.shape.Point)
      return this.equals(other) ? SpatialRelation.INTERSECTS : SpatialRelation.DISJOINT;
    return other.relate(this).transpose();
  }

  public double getX() {
    return pointGeom.getX();
  }

  public double getY() {
    return pointGeom.getY();
  }

  public void reset(double x, double y) {
    CoordinateSequence cSeq = pointGeom.getCoordinateSequence();
    cSeq.setOrdinate(0, CoordinateSequence.X, x);
    cSeq.setOrdinate(0, CoordinateSequence.Y, y);
  }

  @Override
  public String toString() {
    return "Pt(x="+getX()+",y="+getY()+")";
  }

  @Override
  public boolean equals(Object o) {
    return PointImpl.equals(this,o);
  }

  @Override
  public int hashCode() {
    return PointImpl.hashCode(this);
  }
}
