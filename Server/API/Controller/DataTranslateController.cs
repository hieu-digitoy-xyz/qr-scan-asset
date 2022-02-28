using System;
using System.Linq;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Asset_API.Database;
using Asset_API.Models;

namespace Asset_API.Controller
{
    [Route("api/[controller]")]
    [ApiController]
    public class DataTranslateController : ControllerBase
    {
        private readonly TranslateDBContext _context;


        public DataTranslateController(TranslateDBContext context)
        {
            _context = context;
        }

        [HttpGet]
        [AllowAnonymous]
        [Produces("application/json")]
        public IActionResult GetData([FromQuery] string OriginalWord,
                                     [FromQuery] string OriginalLangCode,
                                     [FromQuery] string OriginalLangName,
                                     [FromQuery] string DestinationLangCode,
                                     [FromQuery] string DestinationLangName
                                     )
        {



            // check all fields have data
            if (!string.IsNullOrEmpty(OriginalWord))
            {
                if (!string.IsNullOrEmpty(OriginalLangCode))
                {
                    if (!string.IsNullOrEmpty(OriginalLangName))
                    {
                        if (!string.IsNullOrEmpty(DestinationLangCode))
                        {
                            if (!string.IsNullOrEmpty(DestinationLangName))
                            {
                                var listData = _context.DataTranslates.Where(x =>
                                                      x.OriginalWord.Equals(OriginalWord)
                                                   && x.OriginalLangCode == OriginalLangCode
                                                   && string.Equals(x.OriginalLangName, OriginalLangName)

                                                   && x.DestinationLangCode == DestinationLangCode
                                                   && string.Equals(x.DestinationLangName, DestinationLangName)
                                                ).ToList();

                                DataTranslate data = null;

                                foreach (var item in listData)
                                {
                                    if (string.Equals(item.OriginalWord, OriginalWord))
                                    {
                                        data = item;
                                        break;
                                    }
                                }


                                // do not have already
                                if (data == null)
                                {
                                    // return no data
                                }
                                // have data already
                                else
                                {
                                    // update repeat
                                    var repeatTime = data.Repeat;
                                    // add 1 time
                                    repeatTime = repeatTime + 1;

                                    data.Repeat = repeatTime;
                                    // save
                                    _context.SaveChanges();

                                    // return data
                                    return Ok(data.DestinationWord);
                                }
                            }
                        }
                    }
                }
            }




            return Ok("");
        }

        [HttpPost]
        [AllowAnonymous]
        [Produces("application/json")]
        public IActionResult SaveData([FromQuery] string OriginalWord,
                                     [FromQuery] string OriginalLangCode,
                                     [FromQuery] string OriginalLangName,
                                     [FromQuery] string DestinationWord,
                                     [FromQuery] string DestinationLangCode,
                                     [FromQuery] string DestinationLangName,
                                        [FromQuery] string Type
                                     )
        {
            // check all fields have data
            if (!string.IsNullOrEmpty(OriginalWord))
            {
                if (!string.IsNullOrEmpty(OriginalLangCode))
                {
                    if (!string.IsNullOrEmpty(OriginalLangName))
                    {
                        if (!string.IsNullOrEmpty(DestinationWord))
                        {
                            if (!string.IsNullOrEmpty(DestinationLangCode))
                            {
                                if (!string.IsNullOrEmpty(DestinationLangName))
                                {
                                    var listData = _context.DataTranslates.Where(x =>
                                                          string.Equals(x.OriginalWord, OriginalWord)
                                                       && x.OriginalLangCode == OriginalLangCode
                                                       && string.Equals(x.OriginalLangName, OriginalLangName)

                                                       && string.Equals(x.DestinationWord, DestinationWord)
                                                       && x.DestinationLangCode == DestinationLangCode
                                                       && string.Equals(x.DestinationLangName, DestinationLangName)
                                                    ).ToList();

                                    DataTranslate data = null;

                                    foreach (var item in listData)
                                    {
                                        if (string.Equals(item.OriginalWord, OriginalWord))
                                        {
                                            if (string.Equals(item.DestinationWord, DestinationWord))
                                            {
                                                data = item;
                                                break;
                                            }
                                        }
                                    }


                                    // do not have already
                                    if (data == null)
                                    {
                                        // save
                                        _context.DataTranslates.Add(new DataTranslate()
                                        {
                                            OriginalWord = OriginalWord,
                                            OriginalLangCode = OriginalLangCode,
                                            OriginalLangName = OriginalLangName,
                                            DestinationWord = DestinationWord,
                                            DestinationLangCode = DestinationLangCode,
                                            DestinationLangName = DestinationLangName,
                                            Repeat = 1,
                                            SaveDate = DateTime.Now,
                                            Type = Type
                                        });
                                        _context.SaveChanges();
                                        // return no data
                                        return Ok("SAVE OK");
                                    }
                                    // have data already
                                    else
                                    {


                                        // return data
                                        return Ok("OK");
                                    }
                                }
                            }
                        }
                    }
                }
            }




            return Ok("");
        }
    }
}
