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

        public void ImportData(IFormFile file, [FromServices] IHostingEnvironment hostingEnvironment)
        {
            //string fileName = $"{hostingEnvironment.WebRootPath}\\files\\{file.FileName}";
            var fileName = $"{Directory.GetCurrentDirectory()}{@"\wwwroot\files"}" + "\\" + file.FileName;
            FileStream fileStream = System.IO.File.Create(fileName);

            file.CopyTo(fileStream);
            fileStream.Flush();
            fileStream.Close();

            var assets = this.GetAssetList(file.FileName);
            //return Ok("ok");
        }

        private IActionResult GetAssetList(string fName)
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

            foreach (var item in dataTable.Rows)
            {
                DataRow data = (DataRow)item;
                //try {
                    assets.Add(new Asset()
                    {
                        PartId = data[0] == null ? null : data[0].ToString(),
                        Description = data[1] == null ? null : data[1].ToString(),
                        UseFor = reader.GetValue(2) == null ? null : reader.GetValue(2).ToString(),
                        StorageBin = reader.GetValue(3) == null ? null : reader.GetValue(3).ToString(),
                        //StorageBin = data[8] == null ? null : data[8].ToString(),
                        StockAmount = data[8] == null ? 0 : int.Parse(data[8].ToString()),
                        Search = reader.GetValue(16) == null ? null : reader.GetValue(16).ToString(),
                        Brand = reader.GetValue(17) == null ? null : reader.GetValue(17).ToString(),
                        Local = reader.GetValue(18) == null ? false : true,
                        Import = reader.GetValue(19) == null ? false : true,
                        Note = reader.GetValue(20) == null ? null : reader.GetValue(20).ToString(),
                        AlpSop = reader.GetValue(21) == null ? null : reader.GetValue(21).ToString(),
                        Category = reader.GetValue(22) == null ? null : reader.GetValue(22).ToString(),
                        //Supplier = reader.GetValue(32) == null ? null : reader.GetValue(32).ToString()
                    });
                //} catch(Exception e)
                //{
                //    return BadRequest(data);
                //}
                
            }
           
            return Ok(assets);
        }
    }
}
