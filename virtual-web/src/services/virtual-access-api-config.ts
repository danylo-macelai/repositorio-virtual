/*
 * Description: Customizar axios para o virtual-access
 * Project: virtual-web
 *
 * author: renatoaraujo
 * date: Wed Sep 18 2019 10:45:28 PM
 * version $
 */

import axios from 'axios';

export const virtualAccessConfig = axios.create({
  baseURL: 'http://localhost:3000/access',
});
