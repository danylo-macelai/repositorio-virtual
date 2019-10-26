/*
 * Description: Customiza o axios para o virtual-master
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Fri Jul 26 2019 9:41:35 PM
 * version $
 */

import axios from 'axios';

export const virtualMasterConfig = axios.create({
  baseURL: 'http://localhost:8761/',
});
