using System;
using System.Collections.Generic;

namespace Asset_API.Models
{
    public partial class Asset
    {
        public string PartId { get; set; }
        public string Description { get; set; }
        public string UseFor { get; set; }
        public string StorageBin { get; set; }
        public double? StockAmount { get; set; }
        public string Search { get; set; }
        public string Brand { get; set; }
        public bool? Local { get; set; }
        public bool? Import { get; set; }
        public string Note { get; set; }
        public string AlpSop { get; set; }
        public string Category { get; set; }
        public string Supplier { get; set; }
    }
}
