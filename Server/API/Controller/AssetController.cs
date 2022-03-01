using System;
using System.Linq;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Asset_API.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Hosting;
using System.IO;
using System.Collections.Generic;
using ExcelDataReader;
using System.Data;

namespace Asset_API.Controller
{
    [Route("api/[controller]")]
    [ApiController]
    public class AssetController : ControllerBase
    {
        private readonly AssetDBContext _context;


        public AssetController(AssetDBContext context)
        {
            _context = context;
        }


        [HttpGet]
        [AllowAnonymous]
        [Produces("application/json")]
        public IActionResult GetAsset(string id)
        {
            var asset = _context.Assets.Where(x => x.PartId == id).FirstOrDefault();
            if (asset != null)
            {
                return Ok(asset);
            }
            else
            {
                return BadRequest("Wrong ID");
            }
        }


        [HttpPost]

        public IActionResult ImportData(IFormFile file)
        {
            //string fileName = $"{hostingEnvironment.WebRootPath}\\files\\{file.FileName}";
            var fileName = $"{Directory.GetCurrentDirectory()}{@"\wwwroot\files"}" + "\\" + file.FileName;
            FileStream fileStream = System.IO.File.Create(fileName);

            file.CopyTo(fileStream);
            fileStream.Flush();
            fileStream.Close();

            var assets = this.GetAssetList(file.FileName);

            var number = 0;

            foreach (var asset in assets)
            {
                try
                {
                    var item = _context.Assets.Where(x => x.PartId == asset.PartId).FirstOrDefault();
                    if (item == null)
                    {
                        _context.Assets.Add(asset);
                        _context.SaveChanges();
                        number++;
                    }
                }
                catch (Exception ex)
                {

                }
            }

            return Ok("Import success: " + number + " records");
        }

        private List<Asset> GetAssetList(string fName)
        {
            var assets = new List<Asset>();
            var fileName = $"{Directory.GetCurrentDirectory()}{@"\wwwroot\files"}" + "\\" + fName;
            System.Text.Encoding.RegisterProvider(System.Text.CodePagesEncodingProvider.Instance);
            var stream = System.IO.File.Open(fileName, FileMode.Open, FileAccess.Read);

            var reader = ExcelReaderFactory.CreateReader(stream);

            //// reader.IsFirstRowAsColumnNames
            var conf = new ExcelDataSetConfiguration
            {
                ConfigureDataTable = _ => new ExcelDataTableConfiguration
                {
                    UseHeaderRow = true
                }
            };
            var result = reader.AsDataSet(conf);

            var dataTable = result.Tables[0];

            //List<object> rowDataList = null;
            //List<object> allRowsList = new List<object>();
            //foreach (DataRow item in dataTable.Rows)
            //{
            //    rowDataList = item.ItemArray.ToList(); //list of each rows
            //    allRowsList.Add(rowDataList); //adding the above list of each row to another list
            //}

            //var tmp = new List<string>();

            foreach (var item in dataTable.Rows)
            {
                DataRow data = (DataRow)item;
                //try {
                assets.Add(new Asset()
                {
                    PartId = data[0] == DBNull.Value ? "" : data[0].ToString(),
                    Description = data[1] == DBNull.Value ? "" : data[1].ToString(),
                    UseFor = data[2] == DBNull.Value ? "" : data[2].ToString(),
                    StorageBin = data[4] == DBNull.Value ? "" : data[4].ToString(),
                    //StorageBin = data[8] == null ? null : data[8].ToString(),
                    StockAmount = data[7] == DBNull.Value ? 0 : double.Parse(data[7].ToString()),
                    Search = data[15] == DBNull.Value ? "" : data[15].ToString(),
                    Brand = data[16] == DBNull.Value ? "" : data[16].ToString(),
                    Local = data[17] == DBNull.Value ? false : true,
                    Import = data[18] == DBNull.Value ? false : true,
                    Note = data[19] == DBNull.Value ? "" : data[19].ToString(),
                    AlpSop = data[20] == DBNull.Value ? "" : data[20].ToString(),
                    Category = data[21] == DBNull.Value ? "" : data[21].ToString(),
                    Supplier = data[31] == DBNull.Value ? "" : data[31].ToString()
                });
                //tmp.Add(data[8].ToString());
                //} catch(Exception e)
                //{
                //    return BadRequest(data);
                //}

            }

            return assets;
        }

        [HttpPut]
        public IActionResult UpdateAsset(Asset asset)
        {
            var item = _context.Assets.Where(x => x.PartId == asset.PartId).FirstOrDefault();
            if (item != null)
            {

                return Ok();
            } else
            {
                return BadRequest("Wrong ID");
            }
        }
    }
}
