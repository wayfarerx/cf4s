/* Bucket.scala
 *
 * Copyright (c) 2023 wayfarerx (@x@wayfarerx.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.wayfarerx.cf4s.s3

/**
 * @see http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html
 *
 * @param logicalName The logical name of this resource.
 * @param bucketName http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html#cfn-s3-bucket-name
 * @param websiteConfiguration http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html#cfn-s3-bucket-websiteconfiguration
 */
case class Bucket(
  logicalName: String,
  bucketName: Option[String] = None,
  websiteConfiguration: Option[Bucket.WebsiteConfiguration] = None
) extends net.wayfarerx.cf4s.Resource {

  lazy val arn: net.wayfarerx.cf4s.Data[String] =
    net.wayfarerx.cf4s.Fn.getAtt(logicalName, "Arn")

  lazy val domainName: net.wayfarerx.cf4s.Data[String] =
    net.wayfarerx.cf4s.Fn.getAtt(logicalName, "DomainName")

  lazy val dualStackDomainName: net.wayfarerx.cf4s.Data[String] =
    net.wayfarerx.cf4s.Fn.getAtt(logicalName, "DualStackDomainName")

  lazy val regionalDomainName: net.wayfarerx.cf4s.Data[String] =
    net.wayfarerx.cf4s.Fn.getAtt(logicalName, "RegionalDomainName")

  lazy val websiteURL: net.wayfarerx.cf4s.Data[String] =
    net.wayfarerx.cf4s.Fn.getAtt(logicalName, "WebsiteURL")

  override def resourceType: String = "AWS::S3::Bucket"

  override def resourceProperties: Option[io.circe.Json] = net.wayfarerx.cf4s.Entry.render(
    net.wayfarerx.cf4s.Entry.option("BucketName", bucketName),
    net.wayfarerx.cf4s.Entry.component("WebsiteConfiguration", websiteConfiguration)
  )

}

/**
 * @see http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket.html
 */
object Bucket {

  /**
   *
   * @see http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-websiteconfiguration.html
   *
   * @param errorDocument http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-websiteconfiguration.html#cfn-s3-websiteconfiguration-errordocument
   * @param indexDocument http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-s3-websiteconfiguration.html#cfn-s3-websiteconfiguration-indexdocument
   */
  case class WebsiteConfiguration(
    errorDocument: Option[String] = None,
    indexDocument: Option[String] = None
  ) extends net.wayfarerx.cf4s.Component {

    override def render: Option[io.circe.Json] = net.wayfarerx.cf4s.Entry.render(
      net.wayfarerx.cf4s.Entry.option("ErrorDocument", errorDocument),
      net.wayfarerx.cf4s.Entry.option("IndexDocument", indexDocument)
    )

  }

}
